package com.srp.ewayspanel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.srp.eways.repository.local.PreferenceCache;

import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class MainPreferenceCacheTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {

    }

    @Test
    public void testPrefPutString() {
         PreferenceCache preferenceCache = PreferenceCache.getInstance();

         preferenceCache.put("test", "str1");
         Assert.assertEquals("str1", preferenceCache.getString("test"));
    }

    @Test
    public void testPrefPutListString() {
        PreferenceCache preferenceCache = PreferenceCache.getInstance();

        List<String> stringList = Arrays.asList(new String[]{"str1", "str2", "str1"});

        preferenceCache.put("testList", stringList);
        Assert.assertEquals(stringList, preferenceCache.getStringList("testList"));
    }

    @After
    public void finish() {

    }

}
