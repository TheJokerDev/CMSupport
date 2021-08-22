package net.coralmc.cmsupport.languages;

import xyz.theprogramsrc.supercoreapi.global.translations.Translation;
import xyz.theprogramsrc.supercoreapi.global.translations.TranslationManager;
import xyz.theprogramsrc.supercoreapi.global.translations.TranslationPack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum LBase implements TranslationPack {

    NO_PERMISSION("Messages.NoPermission", "&cYou don't have permissions to do that!");

    private TranslationManager manager;
    private final String content, path;

    LBase(String path, String content){
        this.content = content;
        this.path = path;
    }

    @Override
    public String getLanguage() {
        return "en";
    }

    @Override
    public Translation get() {
        return new Translation(this, this.path, this.content);
    }

    @Override
    public List<Translation> translations() {
        return Arrays.stream(values()).map(LBase::get).collect(Collectors.toList());
    }

    @Override
    public void setManager(TranslationManager translationManager) {
        this.manager = translationManager;
    }

    @Override
    public TranslationManager getManager() {
        return manager;
    }

    @Override
    public String toString() {
        return this.get().translate();
    }
}
