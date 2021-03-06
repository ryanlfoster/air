package com.lonelystorm.air.asset.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.lonelystorm.air.asset.services.LibraryAdapterManager;
import com.lonelystorm.air.asset.util.LibraryConstants;

@Model(adaptables = Resource.class)
public class AssetLibrary extends Asset {

    @Self
    private Resource resource;

    @Inject
    private String[] categories;

    @Inject
    private LibraryAdapterManager libraryAdapterManager;

    private Set<AssetTheme> themes;

    public String[] getCategories() {
        return categories.clone();
    }

    public Set<AssetTheme> getThemes() {
        return Collections.unmodifiableSet(themes);
    }

    @PostConstruct
    protected void construct() {
        setPath(resource.getPath());
        setSources(resource);

        themes = new HashSet<>();
        for (Resource child : resource.getChildren()) {
            if (child.isResourceType(LibraryConstants.ASSET_THEME_TYPE_NAME)) {
                AssetTheme theme = libraryAdapterManager.theme(child);
                if (theme != null) {
                    themes.add(theme);
                }
            }
        }
    }

}
