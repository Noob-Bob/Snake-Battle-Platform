<template>
    <!-- tmplate ref, used to direct access to the underlying dom elements -->
    <div ref="parent" class="gamemap">
        <canvas ref="canvas" tabindex="0">

        </canvas>
    </div>
</template>

<script>
import { GameMap } from '../assets/scripts/GameMap';
import { ref, onMounted } from 'vue';
import { useStore } from 'vuex'

export default {
    setup() {
        const store = useStore();
        let parent = ref(null);
        let canvas = ref(null);

        onMounted(() => { // create a new GameMap object when mounted 
            store.commit("updateGameObject", 
            new GameMap(canvas.value.getContext('2d'), parent.value, store)
            );
        });

        return {
            parent,
            canvas
        }
    }
}
</script>

<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center; /* left-right center */
    align-items: center; /* top-bottom center */
    /* Note that flex can be easilier to implement horizontal and vertical centering */
    /* while margin is not easy to be set to vertical centering */
}
</style>