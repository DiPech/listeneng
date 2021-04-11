<template>
    <div>
        <c-preloader-with-sections-list>
            <template v-if="sectionsList && entries && currentEntry">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card text-center">
                            <div class="card-body">
                                <div class="mb-4">
                                    <small class="text-secondary">
                                        {{ currentIndex + 1 }} / {{ entries.length }}
                                    </small>
                                </div>
                                <button type="button" class="btn btn-outline-secondary btn-lg not-focusable"
                                        :class="{ 'active' : isPlaying }"
                                        @click="toggleState">
                                    <feather type="play-circle" :size="40" v-if="!isPlaying"></feather>
                                    <feather type="pause-circle" :size="40" v-if="isPlaying"></feather>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </template>
        </c-preloader-with-sections-list>
    </div>
</template>

<script>
    import lodash from "lodash";
    import cPreloaderWithSectionsList from "@/scripts/vue/pages/common/preloader-with-sections-list";
    import {waitUntil} from "@/scripts/functions/waiter";
    import AudioPlayer from "@/scripts/service/player";
    import ApiRequest from "@/scripts/api/api-request";
    import {getEntryPart} from "@/scripts/functions/entry";

    export default {
        components: {
            cPreloaderWithSectionsList,
        },
        data() {
            return {
                currentIndex: 0,
                currentEntryPart: "TRANSLATION",
                entries: [],
                isPlaying: false
            }
        },
        computed: {
            sectionsList() {
                return this.$store.state.sectionsList;
            },
            entriesCount() {
                return this.entries.length;
            },
            currentEntry() {
                return this.entries[this.currentIndex];
            },
            stepTimeout() {
                if (this.currentEntryPart === "TRANSLATION") {
                    return 2000;
                }
                return 10000;
            }
        },
        methods: {
            fillEntries() {
                let entries = [];
                for (let section of this.sectionsList) {
                    for (let entry of section.entries) {
                        if (entry.type === "PHRASE") {
                            entries.push(entry);
                        }
                    }
                }
                if (entries.length === 0) {
                    this.$toasted.error("Couldn't find any entries :(");
                    this.$router.push({name: "tree"});
                    return;
                }
                this.entries = lodash.shuffle(entries);
            },
            toggleState() {
                this.isPlaying = !this.isPlaying;
                if (this.isPlaying) {
                    this.startPlaying();
                }
            },
            startPlaying() {
                if (!this.isPlaying) {
                    return;
                }
                let text = getEntryPart(this.currentEntry, this.currentEntryPart);
                let callback = () => {
                    this.nextStep();
                    if (this.isPlaying) {
                        setTimeout(() => {
                            this.startPlaying();
                        }, this.stepTimeout);
                    }
                };
                ApiRequest.getTextSpeech(text)
                    .then((fileName) => {
                        AudioPlayer.play(fileName, callback);
                    })
                    .catch(() => callback());
            },
            nextStep() {
                if (this.currentIndex === this.entries.length - 1 && this.currentEntryPart === "PHRASE") {
                    this.isPlaying = false;
                    this.$router.push({name: "tree"});
                    return;
                }
                if (this.currentEntryPart === "TRANSLATION") {
                    this.currentEntryPart = "PHRASE";
                    return;
                }
                this.currentEntryPart = "TRANSLATION";
                this.currentIndex++;
            }
        },
        mounted() {
            waitUntil(() => this.sectionsList !== null)
                .then(() => this.fillEntries());
        },
        destroyed() {
            this.isPlaying = false;
            this.entries = [];
        }
    }
</script>

<style scoped>

</style>
