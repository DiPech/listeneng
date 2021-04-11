export function getEntryPart(entry, entryPart) {
    if (entryPart === "PHRASE") {
        return entry.phrase;
    }
    if (entryPart === "TRANSLATION") {
        return entry.translation;
    }
    throw new Error("Something went wrong!");
}
