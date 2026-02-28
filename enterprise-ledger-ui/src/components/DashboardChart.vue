<template>
  <div class="chart-card">
    <canvas ref="chartCanvas"></canvas>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue';
import { Chart } from 'chart.js/auto';

const chartCanvas = ref(null);

onMounted(() => {
  new Chart(chartCanvas.value, {
    type: 'line',
    data: {
      labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
      datasets: [
        {
          label: 'Income',
          data: [12000, 15000, 14000, 18000, 17000, 21000],
          borderColor: '#4F8EF7',
          backgroundColor: 'rgba(79,142,247,0.2)',
          tension: 0.4,
          fill: true,
          pointRadius: 4,
          pointBackgroundColor: '#4F8EF7',
        },
        {
          label: 'Expenses',
          data: [8000, 9000, 10000, 11000, 10500, 13000],
          borderColor: '#F87171',
          backgroundColor: 'rgba(248,113,113,0.15)',
          tension: 0.4,
          fill: true,
          pointRadius: 4,
          pointBackgroundColor: '#F87171',
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          labels: {
            color: '#ffffff',
          },
        },
      },
    },
    scales: {
      x: {
        ticks: {
          color: '#A0AEC0',
        },
        grid: {
          color: 'rgba(255,255,255,0.05)',
        },
      },
      y: {
        ticks: {
          color: '#A0AEC0',
        },
        grid: {
          color: 'rgba(255,255,255,0.05)',
        },
      },
    },
  });
});
</script>
<style scoped>
.chart-card {
  background: #142a47;
  border: 1px solid #1e3a5f;
  border-radius: 10px;
  height: 250px;
}
</style>
