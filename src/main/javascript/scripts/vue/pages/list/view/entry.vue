<template>
    <div>
        <template v-if="entry.type === 'TEXT'">
            <div class="text">
                <span v-html="entry.text"></span>
            </div>
        </template>
        <template v-if="entry.type === 'PHRASE'">
            <c-phrase :entry="entry"/>
        </template>
    </div>
</template>

<script>
    import cPhrase from "@/scripts/vue/pages/list/phrase";

    export default {
        components: {
            cPhrase
        },
        props: ["entry", "index"],
        data() {
            return {}
        },
        computed: {},
        methods: {},
        mounted() {

        }
    }
</script>

<style scoped>

</style>
