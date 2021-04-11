export function waitUntil(callback) {
    return new Promise(((resolve) => {
        let resolved = false;
        let interval = setInterval(() => {
            if (callback()) {
                clearInterval(interval);
                if (!resolved) {
                    resolved = true;
                    resolve();
                }
            }
        }, 10);
    }));
}
