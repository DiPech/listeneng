import {ConfigBuilder} from "@/scripts/api/config";
import {GetRequest, PostRequest} from "@/scripts/api/request";

class ApiRequest {

    login(email, password) {
        return new PostRequest("security", "login")
            .perform({email, password});
    }

    register(email, password) {
        return new PostRequest("security", "register")
            .perform({email, password});
    }

    logout() {
        return new GetRequest("security", "logout")
            .setConfig(new ConfigBuilder().silent().build())
            .perform();
    }

    checkSession() {
        return new GetRequest("session", "check")
            .setConfig(new ConfigBuilder().silent().build())
            .perform();
    }

    getCurrentUser() {
        return new GetRequest("user", "current")
            .perform();
    }

    getSectionsTree() {
        return new GetRequest("section", "tree")
            .perform();
    }

    getSectionsList(ids) {
        return new PostRequest("section", "list")
            .perform(ids);
    }

    updateSectionsTree(sectionsTree) {
        return new PostRequest("section", "update-tree")
            .perform(sectionsTree);
    }

    updateSectionsList(sectionsList) {
        return new PostRequest("section", "update-list")
            .perform(sectionsList);
    }

    deleteSections(ids) {
        return new PostRequest("section", "delete")
            .perform(ids);
    }

    getTextSpeech(text) {
        return new PostRequest("speech", "text", text)
            .perform();
    }

    getTextTranslation(text) {
        return new PostRequest("translate", "text", text)
            .perform();
    }

    getEntrySpeech(entryId, entryPart) {
        return new PostRequest("speech", "entry", `${entryId}/${entryPart}`)
            .perform();
    }

    getEntryTranslation(entryId, entryPart) {
        return new PostRequest("translate", "entry", `${entryId}/${entryPart}`)
            .perform();
    }

    exportAll() {
        return new GetRequest("export", "all")
            .perform();
    }

    importAll(data) {
        return new PostRequest("import", "all")
            .perform(data);
    }

    addStatistic(entryId, statisticValue) {
        return new PostRequest("statistic", "add")
            .perform({entryId, statisticValue});
    }

}

const instance = new ApiRequest();

export default instance;
