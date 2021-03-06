package com.lonelystorm.air.asset.sightly;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lonelystorm.air.asset.models.AssetLibrary;
import com.lonelystorm.air.asset.models.Repository;
import com.lonelystorm.air.asset.services.LibraryResolver;
import com.lonelystorm.air.asset.sightly.IncludeAssetLibrary;

@RunWith(MockitoJUnitRunner.class)
public class IncludeAssetLibraryTest {

    @Mock
    private SlingScriptHelper slingScriptHelper;

    @Mock
    private LibraryResolver libraryResolver;

    private IncludeAssetLibrary includeAssetLibrary;

    @Before
    public void setUp() {
        includeAssetLibrary = spy(new IncludeAssetLibrary());

        doReturn(slingScriptHelper).when(includeAssetLibrary).getSlingScriptHelper();
        when(slingScriptHelper.getService(LibraryResolver.class)).thenReturn(libraryResolver);
    }

    @Test
    public void categories() throws Exception {
        doReturn("categories").when(includeAssetLibrary).get("categories", String.class);
        doReturn(null).when(includeAssetLibrary).get("themes", String.class);

        ResourceResolver resolver = Repository.create();
        List<AssetLibrary> libraries = new ArrayList<>();
        libraries.add(resolver.getResource("/library").adaptTo(AssetLibrary.class));
        when(libraryResolver.findLibrariesByCategory("categories")).thenReturn(libraries);

        includeAssetLibrary.activate();
        String include = includeAssetLibrary.include();

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        writer.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"/library.css\">");
        assertEquals(sw.toString(), include);
    }

    @Test
    public void themes() throws Exception {
        doReturn("categories").when(includeAssetLibrary).get("categories", String.class);
        doReturn("blue").when(includeAssetLibrary).get("themes", String.class);

        ResourceResolver resolver = Repository.create();
        List<AssetLibrary> libraries = new ArrayList<>();
        libraries.add(resolver.getResource("/library").adaptTo(AssetLibrary.class));
        when(libraryResolver.findLibrariesByCategory("categories")).thenReturn(libraries);

        includeAssetLibrary.activate();
        String include = includeAssetLibrary.include();

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        writer.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"/library.css\">");
        writer.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"/library/theme1.css\">");
        assertEquals(sw.toString(), include);
    }

}
