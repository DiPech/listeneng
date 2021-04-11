<template>
    <div>
        <c-preloader-with-sections-list>
            <template v-if="sectionsList && entries && currentEntry">
                <div class="row">
                    <div class="col-md-6 offset-md-3">
                        <div class="card text-center flashcard"
                             :class="{ 'turned': currentEntry.turned }"
                             :style="{
                                 'transition': rotationDuration + 's',
                                 'transform': `rotateY(${rotationDegree}deg)`
                             }"
                        >
                            <div class="card-body">
                                <p class="card-text">
                                    <small class="text-muted">
                                        <span v-for="(name, nameIndex) in currentEntry.fullName">
                                            {{ name }}
                                            <feather type="arrow-right" :size="8"
                                                     v-if="nameIndex < currentEntry.fullName.length - 1"/>
                                        </span>
                                    </small>
                                </p>
                                <p class="card-text text">
                                    {{ !currentEntry.turned ? currentEntry.translation : currentEntry.phrase }}
                                </p>
                                <div class="buttons">
                                    <template v-if="!currentEntry.turned">
                                        <button class="btn btn-sm btn-outline-secondary"
                                                @click="turn">
                                            <feather type="rotate-cw" :size="14"/>
                                            Turn
                                        </button>
                                    </template>
                                    <template v-if="currentEntry.turned">
                                        <button class="btn btn-sm btn-outline-danger"
                                                @click="fail">
                                            <feather type="frown" :size="14"/>
                                            Failed
                                        </button>
                                        <button class="btn btn-sm btn-outline-warning ml-1 mr-1"
                                                @click="skip">
                                            <feather type="skip-forward" :size="14"/>
                                            Skip
                                        </button>
                                        <button class="btn btn-sm btn-outline-success"
                                                @click="success">
                                            <feather type="smile" :size="14"/>
                                            Succeeded
                                        </button>
                                    </template>
                                </div>
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
    import ApiRequest from "@/scripts/api/api-request";

    export default {
        components: {
            cPreloaderWithSectionsList,
        },
        data() {
            return {
                currentIndex: 0,
                entries: [],
                rotationDuration: 0.1,
                rotationDegree: 0
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
            rotationDurationMs() {
                return this.rotationDuration * 1000;
            }
        },
        methods: {
            fillEntries() {
                let entries = [];
                for (let section of this.sectionsList) {
                    for (let entry of section.entries) {
                        if (entry.type === "PHRASE") {
                            entries.push({
                                rotationDegree: 0,
                                turned: false,
                                fullName: section.fullName,
                                ...entry
                            });
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
            turn() {
                this.rotationDegree = 90;
                setTimeout(() => {
                    this.entries[this.currentIndex].turned = true;
                    setTimeout(() => {
                        this.rotationDegree = 0;
                    }, this.rotationDurationMs);
                }, this.rotationDurationMs);
            },
            skipAndTurnBack() {
                this.rotationDegree = 90;
                setTimeout(() => {
                    if (this.currentIndex >= this.entriesCount - 1) {
                        this.$router.push({name: "tree"});
                        return;
                    }
                    this.currentIndex = this.currentIndex + 1;
                    setTimeout(() => {
                        this.rotationDegree = 0;
                    }, this.rotationDurationMs);
                }, this.rotationDurationMs);
            },
            finish() {
                this.$router.push({name: "tree"});
            },
            fail() {
                this.markAsFailed();
                this.skipAndTurnBack();
            },
            skip() {
                this.markAsSkipped();
                this.skipAndTurnBack();
            },
            success() {
                this.markAsSuccessful();
                this.skipAndTurnBack();
            },
            addStatistic(value) {
                ApiRequest.addStatistic(this.currentEntry.id, value)
                    .then(() => {
                    })
                    .catch(() => {
                    });
            },
            markAsSkipped() {
                this.addStatistic("SKIPPED");
            },
            markAsFailed() {
                this.addStatistic("FAILURE");
            },
            markAsSuccessful() {
                this.addStatistic("SUCCESS");
            }
        },
        mounted() {
            waitUntil(() => this.sectionsList !== null)
                .then(() => this.fillEntries());
        },
        destroyed() {
            this.entries = [];
        }
    }
</script>

<style scoped>

</style>
