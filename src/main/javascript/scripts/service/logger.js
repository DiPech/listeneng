import {isDev} from "@/scripts/functions/dev";

class Logger {

    log(message, ...optionalParams) {
        if (isDev()) {
            console.log(message, optionalParams);
        }
    }

}

const instance = new Logger();

export default instance;