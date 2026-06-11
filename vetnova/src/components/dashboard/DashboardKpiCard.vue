<template>
  <div
    class="group relative min-h-[132px] p-6 rounded-2xl border border-white/60 overflow-hidden
           shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.06)]
           hover:shadow-[0_8px_32px_rgba(0,0,0,0.12)] hover:-translate-y-1
           transition-all duration-300 ease-out cursor-default"
  >
    <div :class="['absolute inset-0 bg-gradient-to-br opacity-[0.12] group-hover:opacity-[0.18] transition-opacity', gradient]" />
    <div :class="['absolute -top-10 -right-10 w-32 h-32 rounded-full blur-3xl opacity-30 bg-gradient-to-br', gradient]" />

    <div class="relative flex items-start justify-between gap-4">
      <div class="flex-1 min-w-0">
        <p class="text-sm font-medium text-slate-500 tracking-wide">{{ label }}</p>
        <p class="text-4xl font-extrabold text-slate-900 mt-1.5 tracking-tight tabular-nums">{{ value }}</p>
        <div class="mt-4">
          <span
            :class="[
              'inline-flex items-center gap-1 text-xs font-bold px-2.5 py-1 rounded-full shadow-sm',
              growth >= 0
                ? 'bg-emerald-500/10 text-emerald-700 ring-1 ring-emerald-500/20'
                : 'bg-red-500/10 text-red-700 ring-1 ring-red-500/20',
            ]"
          >
            <ArrowTrendingUpIcon v-if="growth >= 0" class="w-3.5 h-3.5" />
            <ArrowTrendingDownIcon v-else class="w-3.5 h-3.5" />
            {{ growth >= 0 ? '+' : '' }}{{ growth }}%
            <span class="font-normal text-slate-400 ml-0.5">vs mes ant.</span>
          </span>
        </div>
      </div>

      <div
        :class="[
          'w-12 h-12 sm:w-[3rem] sm:h-[3rem] rounded-2xl flex items-center justify-center shrink-0',
          'bg-gradient-to-br shadow-lg shadow-black/10 group-hover:scale-110 transition-transform duration-300',
          gradient,
        ]"
      >
        <component :is="icon" class="w-7 h-7 text-white" stroke-width="1.75" />
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  UsersIcon, HeartIcon, CalendarDaysIcon, ClipboardDocumentListIcon,
  ArrowTrendingUpIcon, ArrowTrendingDownIcon,
} from '@heroicons/vue/24/outline'
import { computed } from 'vue'

const props = defineProps({
  label: String,
  value: [String, Number],
  growth: { type: Number, default: 0 },
  iconName: { type: String, default: 'users' },
  gradient: { type: String, default: 'from-blue-600 to-indigo-400' },
})

const icons = {
  users: UsersIcon,
  pets: HeartIcon,
  calendar: CalendarDaysIcon,
  clipboard: ClipboardDocumentListIcon,
}

const icon = computed(() => icons[props.iconName] ?? UsersIcon)
</script>
