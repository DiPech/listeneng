<template>
    <c-draggable tag="div"
                 :root="root" :list="list"
                 class="node" ghost-class="ghost"
                 :group="{ name: 'multilevel-group-1' }"
                 handle=".column-drag"
                 v-bind="draggableComponentOptions">
        <div class="item-group" v-for="(item, itemIndex) in list" :key="rerenderKey + ':' + item.id">
            <div class="content columns">
                <div class="column column-toggle">
                    <input type="checkbox" class="checkbox"
                           v-model="item.checked"
                           @change="onCheckedChange(item, $event.target.checked)">
                </div>
                <div class="column column-drag">
                    <template v-if="isEditMode">
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <feather type="move" :size="18" class="button"></feather>
                    </template>
                </div>
                <div class="column column-name">
                    <div class="name">
                        <template v-if="isEditMode">
                            <div class="form-group">
                                <input type="text" class="form-control"
                                       :class="{ 'is-invalid' : item.name.length < 2 }"
                                       v-model="item.name"
                                       placeholder="Enter a section name...">
                            </div>
                        </template>
                        <template v-if="!isEditMode">
                            {{ item.name }}
                            <template v-if="item.phrasesCount">
                                <span class="small text-secondary d-none d-md-inline-block">
                                    {{ item.phrasesCount }} {{ getPhrasesCountText(item.phrasesCount) }}
                                </span>
                            </template>
                        </template>
                    </div>
                </div>
                <div class="column column-buttons">
                    <div class="buttons d-none d-md-block">
                        <template v-if="isEditMode">
                            <button class="btn btn-outline-primary btn-sm"
                                    @click="edit(item)"
                                    :disabled="item.isNew"
                                    v-tooltip="'Edit the section'">
                                <feather type="edit-2" :size="14"></feather>
                            </button>
                            <button class="btn btn-outline-success btn-sm"
                                    @click="addChild(item)"
                                    v-tooltip="'Create a subsection'">
                                <feather type="plus" :size="14"></feather>
                            </button>
                            <button class="btn btn-outline-danger btn-sm"
                                    v-tooltip="'Remove the section'"
                                    @click="remove(itemIndex)">
                                <feather type="trash" :size="14"></feather>
                            </button>
                        </template>
                        <template v-if="!isEditMode">
                            <button class="btn btn-outline-secondary btn-sm"
                                    @click="view(item)"
                                    v-tooltip="'View the section'">
                                <feather type="eye" :size="14"></feather>
                            </button>
                            <button class="btn btn-outline-secondary btn-sm"
                                    @click="listen(item)"
                                    v-tooltip="'Listen to the phrases'">
                                <feather type="headphones" :size="14"></feather>
                            </button>
                            <button class="btn btn-outline-secondary btn-sm"
                                    @click="learn(item)"
                                    v-tooltip="'Learn phrases'">
                                <feather type="columns" :size="14"></feather>
                            </button>
                        </template>
                    </div>
                </div>
            </div>
            <c-node :root="root" :list="item.children" :child="true" class="subsection"/>
        </div>
    </c-draggable>
</template>

<script>
    import cDraggable from "vuedraggable";
    import {generateUuid, pluralForm} from "@/scripts/functions/common";
    import {fixCheckedStates, getSectionsIds, isFinalSection} from "@/scripts/functions/section";

    export default {
        components: {
            cNode: () => import("@/scripts/vue/pages/tree/node"),
            cDraggable
        },
        props: {
            root: {
                required: true,
                type: Array
            },
            list: {
                required: false,
                type: Array,
                default: null
            },
            child: false
        },
        data() {
            return {
                rerenderKey: Math.random()
            }
        },
        computed: {
            isEditMode() {
                return this.$store.state.isEditMode;
            },
            draggableComponentOptions() {
                return {
                    animation: 100,
                    disabled: !this.isEditMode,
                };
            }
        },
        methods: {
            onCheckedChange(item, state) {
                this.updateChildrenChecked(item, state);
                fixCheckedStates(this.root);
            },
            updateChildrenChecked(section, checked) {
                for (let child of section.children) {
                    child.checked = checked;
                    this.updateChildrenChecked(child, checked);
                }
            },
            addChild(item) {
                item.children.push({
                    checked: false,
                    id: generateUuid(),
                    isNew: true,
                    priority: item.children.length,
                    name: "",
                    phrasesCount: 0,
                    children: [],
                });
            },
            edit(item) {
                this.selectAndGo(item, "list");
            },
            view(item) {
                this.selectAndGo(item, "list");
            },
            listen(item) {
                this.selectAndGo(item, "listen");
            },
            learn(item) {
                this.selectAndGo(item, "cards");
            },
            selectAndGo(item, route) {
                let ids = [];
                if (isFinalSection(item)) {
                    ids.push(item.id);
                } else {
                    getSectionsIds(item).forEach((id) => ids.push(id));
                }
                this.$store.commit("setSelectedSections", ids);
                this.$router.push({name: route});
            },
            remove(itemIndex) {
                this.list.splice(itemIndex, 1);
            },
            getPhrasesCountText(count) {
                return pluralForm(count, "phrase");
            }
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>
