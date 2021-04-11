export function buildTree(array, key, parentKey) {
    let assocArray = makeAssoc(array, key);
    let relations = buildParentChildrenRelations(array, key, parentKey);
    return doBuildTree(0, relations, null, assocArray, key, parentKey);
}

function doBuildTree(level, relations, parent, assocArray) {
    let result = {
        object: assocArray[parent] ? assocArray[parent] : null,
        children: []
    };
    if (relations.hasOwnProperty(parent)) {
        for (let childId of relations[parent]) {
            result.children.push(doBuildTree(level + 1, relations, childId, assocArray));
        }
    }
    if (level === 0) {
        result = result.children;
    }
    return result;
}

function buildParentChildrenRelations(array, key, parentKey) {
    let result = {};
    for (let item of array) {
        let id = item[key];
        let parentId = item[parentKey] ? item[parentKey] : null;
        if (!result.hasOwnProperty(parentId)) {
            result[parentId] = [];
        }
        result[parentId].push(id);
    }
    return result;
}

function makeAssoc(array, key) {
    let result = {};
    for (let item of array) {
        result[item[key]] = item;
    }
    return result;
}
