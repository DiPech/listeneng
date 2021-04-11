import {CONTENT_TYPE_JSON} from "@/scripts/api/constants";

class Config {

    constructor(isSilent, contentType) {
        this._isSilent = isSilent;
        this._contentType = contentType;
    }

    get isSilent() {
        return this._isSilent;
    }

    get contentType() {
        return this._contentType;
    }

}

export class ConfigBuilder {

    constructor() {
        this._isSilent = false;
        this._contentType = CONTENT_TYPE_JSON;
    }

    silent() {
        this._isSilent = true;
        return this;
    }

    contentType(contentType) {
        this._contentType = contentType;
        return this;
    }

    build() {
        return new Config(this._isSilent, this._contentType);
    }

}
