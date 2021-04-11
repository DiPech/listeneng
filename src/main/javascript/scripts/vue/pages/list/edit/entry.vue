<template>
    <div class="entry" :class="[ entry.type ]">
        <div class="drag">
            <feather type="move" :size="14" class="button"></feather>
        </div>
        <div class="delete" @click="remove">
            <feather type="trash" :size="14" class="button"></feather>
        </div>
        <div class="info text-secondary small">
            <template v-if="entry.type === 'TEXT'">
                Text Block
            </template>
            <template v-if="entry.type === 'PHRASE'">
                Phrase and Translation
            </template>
            <template v-if="entry.type === 'DIVIDER'">
                Divider
            </template>
        </div>
        <template v-if="entry.type === 'TEXT'">
            <template v-if="entry.text.length < 2">
                <span v-html="getEntryTextErrorStyles(entry)"></span>
            </template>
            <div :id="'tox-' + entry.id">
                <c-wysiwyg v-model="entry.text" :init="wysiwygConfig"
                           placeholder="Enter some text..."/>
            </div>
        </template>
        <template v-if="entry.type === 'PHRASE'">
            <c-phrase :entry="entry"/>
        </template>
        <template v-if="entry.type === 'DIVIDER'">
            <input type="text" class="form-control"
                   :class="{ 'is-invalid' : entry.divider.length < 2 }"
                   v-model="entry.divider"
                   placeholder="Enter a divider name...">
        </template>
    </div>
</template>

<script>
    import cWysiwyg from "@tinymce/tinymce-vue";
    import cPhrase from "@/scripts/vue/pages/list/phrase";
    import {empty} from "@/scripts/functions/common";

    export default {
        components: {
            cWysiwyg,
            cPhrase,
        },
        props: ["entry"],
        data() {
            return {}
        },
        computed: {
            sectionsList() {
                return this.$store.state.sectionsList;
            },
            wysiwygConfig() {
                return {
                    menubar: false,
                    statusbar: false,
                    branding: false,
                    height: 150,
                    image_dimensions: false,
                };
            }
        },
        methods: {
            remove() {
                this.$emit("removed", this.entry.id);
            },
            empty(object) {
                return empty(object);
            },
            getEntryTextErrorStyles(entry) {
                return `
                <style>
                    #tox-${entry.id} .tox.tox-tinymce {
                        border: 1px solid var(--red);
                    }
                </style>`;
            }
        }
    }
</script>

<style scoped>

</style>
