<template>
    <div class="buttons">
        <button type="button" class="btn btn-outline-secondary btn-sm not-focusable"
                @click="create('TEXT')">
            <feather type="plus" :size="12" class="button"></feather>
            Text
        </button>
        <button type="button" class="btn btn-outline-secondary btn-sm not-focusable ml-1"
                @click="create('PHRASE')">
            <feather type="plus" :size="12" class="button"></feather>
            Phrase
        </button>
        <button type="button" class="btn btn-outline-secondary btn-sm not-focusable ml-1"
                @click="create('DIVIDER')">
            <feather type="plus" :size="12" class="button"></feather>
            Divider
        </button>
    </div>
</template>

<script>
    export default {
        components: {},
        props: [],
        data() {
            return {}
        },
        computed: {},
        methods: {
            create(type) {
                this.$emit("create", type);
            }
        }
    }
</script>

<style scoped>

</style>
