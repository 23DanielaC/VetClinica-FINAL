<template>
  <div
    class="bg-white rounded-2xl border border-slate-100
           shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_20px_rgba(0,0,0,0.05)]
           hover:shadow-[0_4px_24px_rgba(0,0,0,0.09)] transition-shadow duration-300
           p-6 h-full flex flex-col"
  >
    <div class="flex items-start justify-between gap-3 mb-5">
      <div>
        <h3 class="text-base font-bold text-slate-900">{{ title }}</h3>
        <p v-if="subtitle" class="text-xs text-slate-400 mt-0.5">{{ subtitle }}</p>
      </div>
      <slot name="action" />
    </div>

    <div v-if="stats?.length" class="grid grid-cols-2 sm:grid-cols-3 gap-3 mb-5">
      <div
        v-for="stat in stats"
        :key="stat.label"
        class="bg-slate-50/80 rounded-xl px-3 py-2.5 border border-slate-100"
      >
        <p class="text-[11px] font-medium text-slate-400 uppercase tracking-wide">{{ stat.label }}</p>
        <p :class="['text-lg font-bold mt-0.5 tabular-nums', stat.color ?? 'text-slate-800']">{{ stat.value }}</p>
      </div>
    </div>

    <div class="flex-1 min-h-0">
      <slot />
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  subtitle: String,
  stats: { type: Array, default: () => [] },
})
</script>
