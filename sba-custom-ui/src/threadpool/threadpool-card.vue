<template>

  <div class="threadpool-card">
    <!--    <span>{{ threadPool.description }}</span>-->
    <!--    <span>完成任务数：{{ threadPool.completedTaskCount }}</span>-->
    <!--    <span>总任务数：{{ threadPool.taskCount }}</span>-->
    <!--    <el-skeleton animated :loading="loading"/>-->
    <el-card class="box-card">
      <div
          ref="chart"
          style="width: 100%; height: 380px;"/>
    </el-card>
  </div>
</template>
<script>
import echarts from "echarts";
import {formatDateTime} from '@/util/utils'

export default {
  props: {
    threadPool: {
      type: Object,
      required: true
    }
  },
  data: () => ({
    loading: true,
    chart: undefined,
    stateHistoryList: [],
    initTime: formatDateTime(new Date()),
  }),
  mounted() {
    this.initChart(this.$refs.chart);
  },
  methods: {
    initChart(dom) {
      this.chart = echarts.init(dom);
      for (let i = 5; i > 0; i--) {
        this.stateHistoryList.push({
          date: new Date(new Date().getTime() - 1000 * i),
          value: {}
        });
      }
      this.setOption();
    },
    setOption() {
      const option = {
        title: {
          text: this.threadPool.description,
          subtext: `已完成任务数:${this.threadPool.completedTaskCount}, 总任务数:${this.threadPool.taskCount}`,
          textStyle: {
            fontSize: 20
          },
          subtextStyle: {
            fontSize: 15,
            color: '#aaa'
          }
        },
        // color: ['#67C23A','#E6A23C'],
        color: ['#409EFF', '#909399'],
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['运行任务数', '队列任务数'],
          // align: 'right'
          right: 10,
        },
        // dataZoom: [{}],
        xAxis: [
          {
            type: 'time',
            // min: this.initTime,
            splitLine: {
              show: false
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            // name: '任务数'
          }
        ],
        series: [
          {
            name: '运行任务数',
            type: 'line',
            tooltip: {
              valueFormatter: function (value) {
                return value + ' ';
              }
            },
            step: 'start',
            showSymbol: false,
            data: this.activeTaskList
          },
          {
            name: '队列任务数',
            type: 'line',
            tooltip: {
              valueFormatter: function (value) {
                return value + ' ';
              }
            },
            step: 'start',
            showSymbol: false,
            data: this.queueSizeList
          }
        ]
      }
      this.chart.setOption(option);
      this.loading = false;
    },
  },
  computed: {
    activeTaskList() {
      return this.stateHistoryList.map(_ => {
        // return [formatDateTime(_.date), _.value.activeCount]
        return {name: formatDateTime(_.date), value: [formatDateTime(_.date), _.value.activeCount]}
      })
    },
    queueSizeList() {
      return this.stateHistoryList.map(_ => {
        // return [formatDateTime(_.date), _.value.queueSize]
        return {name: formatDateTime(_.date), value: [formatDateTime(_.date), _.value.queueSize]}
      })
    }
  },
  watch: {
    threadPool(newValue, oldValue) {
      this.$nextTick(() => {
        if (this.stateHistoryList.length >= 60) {
          this.stateHistoryList.shift();
        }
        this.stateHistoryList.push({
          date: new Date(),
          value: newValue
        });
        this.setOption();
      })
    }
  },
};
</script>


<style scoped>
.threadpool-card {
  //padding: 10px 0rem;
  margin-bottom: 1rem;
}
</style>
