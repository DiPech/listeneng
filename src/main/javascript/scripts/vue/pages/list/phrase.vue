<template>
    <div class="phrase">
        <div class="input-group phrase">
            <textarea-autosize class="form-control textarea not-focusable" rows="1"
                               placeholder="Enter a phrase..."
                               v-model="entry.phrase"
                               :ref="'ph-' + entry.id"
                               :min-height="38"
                               :disabled="!isEditMode"
                               :class="{ 'is-invalid' : isEditMode && entry.phrase.length < 2 }"
            ></textarea-autosize>
            <div class="input-group-append" v-if="!empty(entry.phrase)">
                <button class="btn btn-outline-secondary not-focusable listen" type="button"
                        v-tooltip="'Listen to the phrase'"
                        @click="listen(entry, 'PHRASE')">
                    <feather type="headphones" :size="14" v-if="!performingPhraseListen"></feather>
                    <feather type="refresh-cw" :size="14" animation="spin" v-if="performingPhraseListen"></feather>
                </button>
            </div>
            <div class="input-group-append"
                 v-if="isEditMode && empty(entry.phrase) && !empty(entry.translation)">
                <button class="btn btn-outline-secondary not-focusable translate" type="button"
                        v-tooltip="'Translate the translation to get a phrase'"
                        @click="translate(entry, 'TRANSLATION', (result) => entry.phrase = result)">
                    <feather type="globe" :size="14" v-if="!performingTranslationTranslate"></feather>
                    <feather type="refresh-cw" :size="14" animation="spin"
                             v-if="performingTranslationTranslate"></feather>
                </button>
            </div>
        </div>
        <div class="input-group translation">
            <div class="input-group-prepend"
                 v-if="isEditMode && empty(entry.translation) && !empty(entry.phrase)">
                <button class="btn btn-outline-secondary not-focusable translate" type="button"
                        v-tooltip="'Translate the phrase to get a translation'"
                        @click="translate(entry, 'PHRASE', (result) => entry.translation = result)">
                    <feather type="globe" :size="14" v-if="!performingPhraseTranslate"></feather>
                    <feather type="refresh-cw" :size="14" animation="spin" v-if="performingPhraseTranslate"></feather>
                </button>
            </div>
            <div class="input-group-prepend" v-if="!empty(entry.translation)">
                <button class="btn btn-outline-secondary not-focusable listen" type="button"
                        v-tooltip="'Listen to the translation'"
                        @click="listen(entry, 'TRANSLATION')">
                    <feather type="headphones" :size="14" v-if="!performingTranslationListen"></feather>
                    <feather type="refresh-cw" :size="14" animation="spin" v-if="performingTranslationListen"></feather>
                </button>
            </div>
            <textarea-autosize class="form-control textarea not-focusable" rows="1"
                               placeholder="Enter a translation..."
                               v-model="entry.translation"
                               :ref="'tr-' + entry.id"
                               :min-height="38"
                               :disabled="!isEditMode"
                               :class="{ 'is-invalid' : isEditMode && entry.translation.length < 2 }"
            ></textarea-autosize>
        </div>
    </div>
</template>

<script>
    import {empty} from "@/scripts/functions/common";
    import ApiRequest from "@/scripts/api/api-request";
    import AudioPlayer from "@/scripts/service/player";
    import {getEntryPart} from "@/scripts/functions/entry";

    export default {
        components: {},
        props: ["entry"],
        data() {
            return {
                performingPhraseListen: false,
                performingTranslationTranslate: false,
                performingPhraseTranslate: false,
                performingTranslationListen: false,
            }
        },
        computed: {
            isEditMode() {
                return this.$store.state.isEditMode;
            },
        },
        methods: {
            empty(object) {
                return empty(object);
            },
            markPerforming(type, entryType, value) {
                if (type === "LISTEN") {
                    if (entryType === "PHRASE") {
                        this.performingPhraseListen = value;
                    }
                    if (entryType === "TRANSLATION") {
                        this.performingTranslationListen = value;
                    }
                }
                if (type === "TRANSLATE") {
                    if (entryType === "PHRASE") {
                        this.performingPhraseTranslate = value;
                    }
                    if (entryType === "TRANSLATION") {
                        this.performingTranslationTranslate = value;
                    }
                }
            },
            listen(entry, entryPart) {
                this.markPerforming("LISTEN", entryPart, true);
                // @todo use ApiRequest.getEntrySpeech in view mode
                let text = getEntryPart(entry, entryPart);
                ApiRequest.getTextSpeech(text)
                    .then((fileName) => {
                        AudioPlayer.play(fileName);
                        this.markPerforming("LISTEN", entryPart, false);
                    })
                    .catch(() => {
                        this.markPerforming("LISTEN", entryPart, false);
                    });
            },
            translate(entry, entryPart, callback) {
                this.markPerforming("TRANSLATE", entryPart, true);
                // @todo use ApiRequest.getEntryTranslation in view mode
                let text = getEntryPart(entry, entryPart);
                ApiRequest.getTextTranslation(text)
                    .then((result) => {
                        callback(result);
                        this.markPerforming("TRANSLATE", entryPart, false);
                    })
                    .catch(() => {
                        this.markPerforming("TRANSLATE", entryPart, false);
                    });
            }
        },
        mounted() {
            window.addEventListener("resize", () => {
                this.$refs["ph-" + this.entry.id].resize();
                this.$refs["tr-" + this.entry.id].resize();
            });
        }
    }
</script>

<style scoped>

</style>
