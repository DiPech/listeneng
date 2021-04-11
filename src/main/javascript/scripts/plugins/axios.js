import axios from "axios";
import {getBaseUrl} from "@/scripts/functions/common";

let axiosSettings = {
    withCredentials: true
}
if (getBaseUrl().length > 0) {
    axiosSettings.baseURL = getBaseUrl();
}

const instance = axios.create(axiosSettings);

export default instance;
