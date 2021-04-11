import {v4 as uuidv4} from "uuid";
import lodash from "lodash";

import {isDev} from "@/scripts/functions/dev";

export function getBaseUrl() {
    return isDev() ? `http://${APP_DOMAIN}:8080` : "";
}

export function getStorageFileUrl(fileName) {
    return getBaseUrl() + "/storage/" + fileName;
}

export function getDownloadFileUrl(fileName) {
    return getBaseUrl() + "/api/download/file/" + fileName;
}

export function openInNewTab(url) {
    window.open(url, "_blank").focus();
}

export function download(fileName) {
    openInNewTab(getDownloadFileUrl(fileName));
}

export function generateUuid() {
    return uuidv4();
}

export function pluralForm(quantity, word) {
    return word + (quantity > 1 ? "s" : "");
}

export function clone(object) {
    return lodash.cloneDeep(object);
}

export function equals(object1, object2) {
    return lodash.isEqual(object1, object2);
}

export function empty(object) {
    return lodash.isEmpty(object);
}
