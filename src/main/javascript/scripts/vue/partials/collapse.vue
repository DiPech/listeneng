<template>
    <div class="accordion" :class="{ 'compact' : compact }" :id="'accordion' + keyword">
        <div class="card">
            <div class="card-header" :id="'accordion-header' + keyword">
                <h2 class="mb-0">
                    <button type="button"
                            class="btn btn-block text-left not-focusable"
                            data-toggle="collapse" aria-expanded="true"
                            :data-target="'#collapse' + keyword"
                            :aria-controls="'collapse' + keyword">
                        <slot name="header"></slot>
                    </button>
                </h2>
            </div>
            <div :id="'collapse' + keyword" class="collapse show"
                 :aria-labelledby="'accordion-header' + keyword"
                 :data-parent="'#accordion' + keyword">
                <div class="card-body">
                    <slot name="body"></slot>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {generateUuid} from "@/scripts/functions/common";

    export default {
        props: ["compact"],
        data() {
            return {
                keyword: null,
            }
        },
        mounted() {
            this.keyword = generateUuid();
        }
    }
</script>

<style scoped>

</style>
