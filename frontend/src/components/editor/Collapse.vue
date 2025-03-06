<template>
  <div class="custom-collapse">
    <div class="flex items-center justify-between gap-2">
      <div @click="toggle">
        <FontAwesomeIcon :icon="faChevronRight" v-if="!isOpen" />
        <FontAwesomeIcon :icon="faChevronDown" v-if="isOpen" />
        {{ props.collapseTitle }}
      </div>
      <slot name="button" class="w-full" />
    </div>

    <slot v-if="isOpen" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faChevronDown, faChevronRight } from '@fortawesome/free-solid-svg-icons'

const props = defineProps(['collapseTitle'])

const isOpen = ref(false)

const toggle = () => {
  isOpen.value = !isOpen.value
}
</script>