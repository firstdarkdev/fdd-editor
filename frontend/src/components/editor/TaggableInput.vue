<template>
  <span class="taggable-input flex items-centerbg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
    <span v-for="tag in displayedTags" :key="tag" class="inline-block mr-2 bg-blue-500 text-white rounded px-2 py-1">
      {{ tag }}
      <button @click="removeTag(tag)" class="text-white hover:opacity-75 focus:outline-none">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l5.293-5.293a1 1 0 111.414 1.414L11.414 10l5.293 5.293a1 1 0 01-1.414 1.414L10 11.414l-5.293 5.293a1 1 0 01-1.414-1.414L8.586 10L3.293 4.293z" clip-rule="evenodd" />
        </svg>
      </button>
    </span>
    <span v-if="inputValue" class="inline-block ml-2">{{ inputValue }}</span>
  </span>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue';

export default defineComponent({
  name: 'TaggableInput',
  data() {
    return {
      inputValue: '',
      tags: [] as string[],
    };
  },
  methods: {
    addTag() {
      if (this.inputValue.trim()) {
        this.tags.push(this.inputValue.trim());
        this.inputValue = '';
      }
    },
    removeTag(tag: string) {
      const index = this.tags.indexOf(tag);
      if (index > -1) {
        this.tags.splice(index, 1);
      }
    },
  },
  computed: {
    displayedTags() {
      return [...this.tags, this.inputValue.trim()];
    },
  },
});
</script>

<style scoped>
.taggable-input span {
  white-space: nowrap; /* Prevent wrapping of content */
}

.taggable-input span + span {
  margin-left: 8px; /* Add spacing between tags */
}
</style>
