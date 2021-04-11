<template>
    <c-collapse class="section">
        <template v-slot:header>
            <h5>
                <span v-for="(name, nameIndex) in section.fullName">
                    {{ name }}
                    <feather type="arrow-right" :size="14"
                             v-if="nameIndex < section.fullName.length - 1"
                    ></feather>
                </span>
            </h5>
        </template>
        <template v-slot:body>
            <div :class="{ 'pt-3': isEditMode }">
                <slot></slot>
            </div>
        </template>
    </c-collapse>
</template>

<script>
    import cCollapse from "@/scripts/vue/partials/collapse";

    export default {
        components: {
            cCollapse
        },
        props: ["section"],
        data() {
            return {}
        },
        computed: {
            isEditMode() {
                return this.$store.state.isEditMode;
            },
        },
        methods: {},
        mounted() {
        }
    }
</script>

<style scoped>

</style>
