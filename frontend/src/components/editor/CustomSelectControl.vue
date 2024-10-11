<template>
  <div ref="dropdownContainer" class="relative inline-block w-64">
    <button
      @click="toggleDropdown"
      :class="customClass"
      class="relative"
    >
      {{ selectedLabel || 'Select an option' }}
      <span class="absolute end-2 top-2">&#x25BC;</span>
    </button>
    <ul
      v-if="isOpen"
      class="absolute w-full bg-white text-black dropbg rounded shadow-lg dlbox p-1"
    >
      <li
        v-for="option in options"
        :key="option.value"
        @click="selectOption(option)"
        class="px-2 py-1 hover:bg-blue-500 hover:text-white dark:text-white cursor-pointer rounded text-sm"
      >
        {{ option.label }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue';

const props = defineProps({
  modelValue: String,
  options: Array, // Array of options passed as a prop
});

const emit = defineEmits(['update:modelValue']);

const isOpen = ref(false);
const selectedLabel = ref('');
const dropdownContainer = ref(null);

const customClass = 'border text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full bg-ct-light-secondary dark:bg-ct-dark-secondary border-ct-card dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500 py-2 text-left px-3';

const toggleDropdown = () => {
  isOpen.value = !isOpen.value;
};

const selectOption = (option) => {
  emit('update:modelValue', option.value);
  selectedLabel.value = option.label;
  isOpen.value = false;
};

watch(
  () => props.modelValue,
  (newValue) => {
    const selectedOption = props.options.find(option => option.value === newValue);
    if (selectedOption) {
      selectedLabel.value = selectedOption.label;
    }
  },
  { immediate: true }
);

onMounted(() => {
  const selectedOption = props.options.find(option => option.value === props.modelValue);
  if (selectedOption) {
    selectedLabel.value = selectedOption.label;
  }

  // Add event listener for clicks outside the dropdown
  document.addEventListener('mousedown', handleClickOutside);
});

onBeforeUnmount(() => {
  // Remove event listener when component is unmounted
  document.removeEventListener('mousedown', handleClickOutside);
});

const handleClickOutside = (event) => {
  if (dropdownContainer.value && !dropdownContainer.value.contains(event.target)) {
    isOpen.value = false;
  }
};
</script>

<style scoped>
.dlbox {
  z-index: 9999999999;
}

.dark .dropbg {
  background: #212349;
  border-width: 1px;
  border-color: rgba(255, 255, 255, .1) !important;
}
</style>
