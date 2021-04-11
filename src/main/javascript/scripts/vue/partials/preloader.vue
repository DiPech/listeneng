<template>
    <div class="d-inline">
        <div class="text-center" v-if="!condition">
            <feather type="refresh-cw" animation="spin"></feather>
        </div>
        <slot v-if="condition"></slot>
    </div>
</template>

<script>
    export default {
        props: ["condition"]
    }
</script>

<style scoped>

</style>
