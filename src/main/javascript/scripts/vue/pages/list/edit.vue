<template>
    <div class="entries">
        <c-draggable tag="div"
                     v-model="elements"
                     :class="draggingClass"
                     ghost-class="ghost"
                     :group="{ name: 'entries-list-group' }"
                     handle=".drag"
                     v-bind="draggableComponentOptions"
                     @start="onStart"
                     @end="onEnd">
            <div v-for="(element, elementIndex) in elements">
                <template v-if="element.type === 'ENTRY'">
                    <c-entry :entry="element.entry" @removed="removeEntryById" :key="elementIndex"/>
                </template>
                <template v-if="element.type === 'BUTTONS'">
                    <c-buttons :key="elementIndex" @create="createNewElement(elementIndex, $event)"/>
                </template>
            </div>
        </c-draggable>
    </div>
</template>

<script>
    import cDraggable from "vuedraggable";
    import cEntry from "@/scripts/vue/pages/list/edit/entry";
    import cButtons from "@/scripts/vue/pages/list/edit/buttons";
    import {generateUuid} from "@/scripts/functions/common";

    export default {
        components: {
            cDraggable,
            cEntry,
            cButtons
        },
        props: ["section"],
        data() {
            return {
                elements: null,
                dragging: false,
                performing: false
            }
        },
        computed: {
            draggableComponentOptions() {
                return {
                    animation: 100
                };
            },
            draggingClass() {
                return this.dragging ? "dragging" : "";
            }
        },
        methods: {
            onStart() {
                this.dragging = true;
            },
            onEnd() {
                this.fixButtonsPositionsInElements();
                this.dragging = false;
            },
            convertEntriesIntoElements(entries) {
                let result = [];
                for (let entry of entries) {
                    result.push({type: "ENTRY", entry});
                }
                return result;
            },
            addButtonsBeforeAfterAndBetweenElements(elements) {
                let result = [];
                result.push({type: "BUTTONS"});
                for (let element of elements) {
                    result.push(element);
                    result.push({type: "BUTTONS"});
                }
                return result;
            },
            getOnlyElementsOfTypeEntry() {
                let result = [];
                for (let element of this.elements) {
                    if (element.type === "ENTRY") {
                        result.push(element);
                    }
                }
                return result;
            },
            fixButtonsPositionsInElements() {
                let onlyEntries = this.getOnlyElementsOfTypeEntry();
                this.elements = this.addButtonsBeforeAfterAndBetweenElements(onlyEntries);
            },
            convertElementsIntoEntries() {
                let elementsOfTypeEntry = this.getOnlyElementsOfTypeEntry();
                let result = [];
                for (let element of elementsOfTypeEntry) {
                    result.push(element.entry);
                }
                return result;
            },
            removeEntryById(entryId) {
                let elementIndex = null;
                for (let index in this.elements) {
                    let element = this.elements[index];
                    if (element.type === "ENTRY" && element.entry.id === entryId) {
                        elementIndex = index;
                    }
                }
                if (elementIndex !== null) {
                    this.elements.splice(elementIndex, 1);
                }
                this.fixButtonsPositionsInElements();
            },
            createNewElement(index, type) {
                let tail = this.elements.slice(0, index);
                let head = this.elements.slice(index + 1);
                let entry = this.createEntryByType(type);
                tail.push({type: "ENTRY", entry});
                this.elements = tail.concat(head);
                this.fixButtonsPositionsInElements();
            },
            createEntryByType(type) {
                return {
                    id: generateUuid(),
                    type,
                    text: type === "TEXT" ? "" : null,
                    phrase: type === "PHRASE" ? "" : null,
                    translation: type === "PHRASE" ? "" : null,
                    divider: type === "DIVIDER" ? "" : null,
                };
            }
        },
        mounted() {
            let onlyElements = this.convertEntriesIntoElements(this.section.entries);
            this.elements = this.addButtonsBeforeAfterAndBetweenElements(onlyElements);
        },
        updated() {
            this.section.entries = this.convertElementsIntoEntries();
        }
    }
</script>

<style scoped>

</style>
