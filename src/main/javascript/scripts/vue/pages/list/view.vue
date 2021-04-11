<template>
    <div>
        <div v-for="(group, groupIndex) in getDividedEntriesGroups(section.entries)">
            <template v-if="groupIndex === 0">
                <c-entry :entry="entryData.entry" :index="entryData.index"
                         v-for="entryData in group.entries" :key="entryData.index"></c-entry>
            </template>
            <template v-if="groupIndex > 0">
                <c-collapse :compact="true">
                    <template v-slot:header>
                        <span v-html="group.name"></span>
                    </template>
                    <template v-slot:body>
                        <c-entry :entry="entryData.entry" :index="entryData.index"
                                 v-for="entryData in group.entries"
                                 :key="entryData.index"></c-entry>
                    </template>
                </c-collapse>
            </template>
        </div>
    </div>
</template>

<script>
    import cCollapse from "@/scripts/vue/partials/collapse";
    import cEntry from "@/scripts/vue/pages/list/view/entry";

    export default {
        components: {
            cCollapse,
            cEntry,
        },
        props: ["section"],
        data() {
            return {}
        },
        computed: {},
        methods: {
            getDividedEntriesGroups(entries) {
                let groups = [];
                let group = null;
                let groupName = null;
                for (let index in entries) {
                    let entry = entries[index];
                    if (group === null) {
                        group = {
                            name: groupName,
                            entries: []
                        };
                    }
                    let groupIndex = group.entries.length;
                    group.entries.push({entry, index: groupIndex});
                    if (entry.type === "DIVIDER" || parseInt(index) === entries.length - 1) {
                        groups.push(group);
                        group = null;
                        groupName = entry.divider;
                    }
                }
                return groups;
            }
        }
    }
</script>

<style scoped>

</style>
