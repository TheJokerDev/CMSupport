package net.coralmc.cmsupport.languages;

import xyz.theprogramsrc.supercoreapi.global.translations.Translation;
import xyz.theprogramsrc.supercoreapi.global.translations.TranslationManager;
import xyz.theprogramsrc.supercoreapi.global.translations.TranslationPack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum LBase implements TranslationPack {
    ERROR_WHILE_CREATING_TABLES("Errors.CreateTable","Cannot create tables:"),
    ERROR_WHILE_UPDATING_TABLES("Errors.UpdateTable","Cannot update tables:"),
    ERROR_WHILE_DELETING_USER("Errors.DeleteUser","Couldn't delete user:"),
    ERROR_WHILE_SAVING_USER_DATA("Errors.SaveUser","Error while saving {UserName}'s data:"),
    ERROR_WHILE_HASHING_PASSWORD("Errors.HashPassword","Error while hashing password:"),
    ERROR_ON_DATA_REQUEST("Errors.FailedRequest","Couldn't request data:"),

    ERROR_ON_ADD_PARTNER("Errors.AddPartner","&cCouldn't add that partner"),
    ERROR_ON_REMOVE_PARTNER("Errors.RemovePartner","&cCouldn't remove that partner"),

    USE_ADMIN_COMMAND("Messages.UseAdminCommand", "&cUse: /supportadmin partner add/remove <nick>."),

    ADDED_PARTNER("Messages.AddedPartner", "&aPartner &eadded &asuccessfully!"),
    PARTNER_EXIST("Messages.PartnerExist", "&cThat partner already exists!"),
    REMOVED_PARTNER("Messages.RemovedPartner", "&aPartner &cremoved &asuccessfully!"),
    PARTNER_NOT_EXIST("Messages.PartnerNotExist", "&cThat partner not exists!"),
    VOTES_RESET("Messages.VotesReset", "&cVotes resseted for that partner!"),
    VOTES_RESET_ALL("Messages.VotesResetAll", "&cVotes resseted for all partners!"),


    ALREADY_VOTED("Messages.AlreadyVoted", "&cYou've already voted for a partner!"),
    VOTED_SUCCESSFULLY("Messages.VotedSuccessfully", "&aVoted successfully for {partner}!"),
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
