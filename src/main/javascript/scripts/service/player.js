import {getStorageFileUrl} from "@/scripts/functions/common";

class AudioPlayer {

    play(fileName, callback) {
        let audio = new Audio(getStorageFileUrl(fileName));
        audio.onended = function () {
            callback();
        }
        audio.play();
    }

}

const instance = new AudioPlayer();

export default instance;
