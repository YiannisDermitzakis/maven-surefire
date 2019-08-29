package org.apache.maven.surefire.common.junit48;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.surefire.common.junit48.tests.group.ABCParameterizedTest;
import org.apache.maven.surefire.common.junit48.tests.group.ABCTest;
import org.apache.maven.surefire.common.junit48.tests.group.ATest;
import org.apache.maven.surefire.common.junit48.tests.group.BBCTest;
import org.apache.maven.surefire.common.junit48.tests.group.BCTest;
import org.apache.maven.surefire.common.junit48.tests.group.BTest;
import org.apache.maven.surefire.group.match.GroupMatcher;
import org.apache.maven.surefire.group.match.SingleGroupMatcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Description;

/**
 * Tests covering inheritance in @Categories for Test classes.
 */
public class GroupMatcherCategoryFilterTest
{

    private GroupMatcherCategoryFilter cut;

    @Test
    public void shouldMatchIncludedCategoryInSelf()
    {
        GroupMatcher included =
            new SingleGroupMatcher( "org.apache.maven.surefire.common.junit48.tests.group.marker.CategoryB" );
        GroupMatcher excluded = null;
        cut = new GroupMatcherCategoryFilter( included, excluded );
        Assert.assertTrue( cut.shouldRun( Description.createSuiteDescription( BTest.class ) ) );
    }

    @Test
    public void shouldMatchIncludedCategoryInParent()
    {
        GroupMatcher included =
            new SingleGroupMatcher( "org.apache.maven.surefire.common.junit48.tests.group.marker.CategoryB" );
        GroupMatcher excluded = null;
        cut = new GroupMatcherCategoryFilter( included, excluded );
        Assert.assertTrue( cut.shouldRun( Description.createSuiteDescription( BCTest.class ) ) );
        Assert.assertFalse( cut.shouldRun( Description.createSuiteDescription( ATest.class ) ) );
    }

    @Test
    public void shouldMatchIncludedCategoryInHierarchy()
    {
        GroupMatcher included =
            new SingleGroupMatcher( "org.apache.maven.surefire.common.junit48.tests.group.marker.CategoryC" );
        GroupMatcher excluded = null;
        cut = new GroupMatcherCategoryFilter( included, excluded );
        Assert.assertTrue( cut.shouldRun( Description.createSuiteDescription( ABCTest.class ) ) );
        Assert.assertTrue( cut.shouldRun( Description.createSuiteDescription( BCTest.class ) ) );
        Assert.assertFalse( cut.shouldRun( Description.createSuiteDescription( ATest.class ) ) );
    }

    @Test
    public void shouldMatchIncludedCategoryInParentWhenSelfHasAnother()
    {
        GroupMatcher included =
            new SingleGroupMatcher( "org.apache.maven.surefire.common.junit48.tests.group.marker.CategoryB" );
        GroupMatcher excluded = null;
        cut = new GroupMatcherCategoryFilter( included, excluded );
        Assert.assertTrue( cut.shouldRun( Description.createSuiteDescription( ABCTest.class ) ) );
        Assert.assertTrue( cut.shouldRun( Description.createTestDescription( ABCTest.class, "abc" ) ) );
        Assert.assertTrue( cut.shouldRun( Description.createSuiteDescription( ABCParameterizedTest.class ) ) );
        Assert.assertTrue( cut.shouldRun( Description.createTestDescription( ABCParameterizedTest.class, "abc" ) ) );
    }

    @Test
    public void shouldNotMatchIncludedCategoryInParentWhenSelfHasExcludedCategory()
    {
        GroupMatcher included =
            new SingleGroupMatcher( "org.apache.maven.surefire.common.junit48.tests.group.marker.CategoryB" );
        GroupMatcher excluded =
            new SingleGroupMatcher( "org.apache.maven.surefire.common.junit48.tests.group.marker.CategoryA" );
        cut = new GroupMatcherCategoryFilter( included, excluded );
        Assert.assertFalse( cut.shouldRun( Description.createSuiteDescription( ABCTest.class ) ) );
        Assert.assertTrue( cut.shouldRun( Description.createSuiteDescription( BBCTest.class ) ) );
        Assert.assertTrue( cut.shouldRun( Description.createSuiteDescription( BTest.class ) ) );
    }

    @Test
    public void shouldMatchExcludedCategoryInSelf()
    {
        GroupMatcher included = null;
        GroupMatcher excluded =
            new SingleGroupMatcher( "org.apache.maven.surefire.common.junit48.tests.group.marker.CategoryA" );
        cut = new GroupMatcherCategoryFilter( included, excluded );
        Assert.assertFalse( cut.shouldRun( Description.createSuiteDescription( ATest.class ) ) );
        Assert.assertTrue( cut.shouldRun( Description.createSuiteDescription( BTest.class ) ) );
        Assert.assertTrue( cut.shouldRun( Description.createSuiteDescription( BBCTest.class ) ) );
    }
}