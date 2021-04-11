import {clone, equals} from "@/scripts/functions/common";

export function addRequiredFields(sections) {
    for (let section of sections) {
        section.checked = false;
        addRequiredFields(section.children);
    }
    return sections;
}

export function removeUntrackableFields(sectionsTree) {
    for (let section of sectionsTree) {
        delete section.checked;
        removeUntrackableFields(section.children);
    }
    return sectionsTree;
}

export function isSectionsTreesEqual(sections1, sections2) {
    let first = clone(sections1);
    let second = clone(sections2);
    removeUntrackableFields(first);
    removeUntrackableFields(second);
    return equals(first, second);
}

export function findSectionByProperty(sections, property, value) {
    for (let section of sections) {
        if (section[property] === value) {
            return section;
        }
        let result = findSectionByProperty(section.children, property, value);
        if (result) {
            return result;
        }
    }
    return null;
}

export function fixCheckedStates(sections) {
    let levelledList = buildLevelledSectionsList(sections, 0);
    levelledList.sort((a, b) => (a.level < b.level) ? 1 : -1)
    for (let data of levelledList) {
        if (data.section.children.length === 0) {
            continue;
        }
        let uncheckedCount = calcSectionUncheckedChildren(data.section);
        if (uncheckedCount > 0) {
            if (data.section.checked) {
                data.section.checked = false;
            }
        } else {
            if (!data.section.checked) {
                data.section.checked = true;
            }
        }
    }
}

export function isFinalSection(section) {
    return section.children.length === 0;
}

export function getSectionsIds(section) {
    let result = [section.id];
    for (let child of section.children) {
        result.push(child.id);
        result = result.concat(getSectionsIds(child));
    }
    return [...new Set(result)];
}

function buildLevelledSectionsList(sections, level) {
    let result = [];
    for (let section of sections) {
        result.push({section, level});
        result = result.concat(buildLevelledSectionsList(section.children, level + 1));
    }
    return result;
}

function calcSectionChildrenChecks(section, state) {
    let count = 0;
    for (let child of section.children) {
        if (child.checked === state) {
            count++;
        }
        count += calcSectionChildrenChecks(child, state);
    }
    return count;
}

function calcSectionUncheckedChildren(section) {
    return calcSectionChildrenChecks(section, false);
}

function calcSectionCheckedChildren(section) {
    return calcSectionChildrenChecks(section, true);
}

export function calcSectionsCheckedChildren(sections) {
    let count = 0;
    for (let section of sections) {
        if (section.checked) {
            count++;
        }
        count += calcSectionCheckedChildren(section);
    }
    return count;
}

export function getSelectedSectionsIds(sections) {
    let result = [];
    for (let section of sections) {
        if (section.checked) {
            result.push(section.id);
        }
        result = result.concat(getSelectedSectionsIds(section.children));
    }
    return result;
}

export function deselectAllSections(sections) {
    for (let section of sections) {
        section.checked = false;
        deselectAllSections(section.children);
    }
}
