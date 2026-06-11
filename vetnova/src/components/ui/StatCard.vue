<template>
  <div class="card flex items-center gap-4">
    <div :class="`w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0 ${colorClass}`">
      <component :is="icon" class="w-6 h-6 text-white" />
    </div>
    <div>
      <p class="text-sm text-gray-500">{{ label }}</p>
      <p class="text-2xl font-bold text-gray-800">{{ value }}</p>
    </div>
  </div>
</template>
<script setup>
defineProps({ icon: Object, label: String, value: [String, Number], colorClass: { default: 'bg-primary' } })
</script>
