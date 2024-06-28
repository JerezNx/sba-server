<!--
  - Copyright 2014-2018 the original author or authors.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <div class="task-log">
    <!--    <iframe :src="iframeUrl" style="width: 100%;height: 100%"></iframe>-->
    <Card style="height: 840px;">
      <Form :model="condition" :label-width="120" class="list-search-form">
        <Row>
          <Col span="6">
            <form-item label="名称">
              <Input v-model.trim="condition.name"/>
            </form-item>
          </Col>
          <Col span="6">
            <form-item label="方法名称">
              <Input v-model.trim="condition.methodName"/>
            </form-item>
          </Col>
          <Col span="6">
            <form-item label="类型">
              <i-select v-model="condition.execStatus" placeholder="--请选择类型--" clearable>
                <i-option :value="0">执行中</i-option>
                <i-option :value="1">执行成功</i-option>
                <i-option :value="2">执行失败</i-option>
              </i-select>
            </form-item>
          </Col>
          <Col span="6" class="search-button">
            <i-button type="primary" @click="search">查询</i-button>
            <i-button @click="reset" style="margin-left: 10px">重置</i-button>
          </Col>
          <Col span="6">
            <form-item label="执行实例">
              <Input v-model.trim="condition.instanceId"/>
            </form-item>
          </Col>
          <Col span="6">
            <form-item label="是否并行执行">
              <i-select v-model="condition.allowParallel" placeholder="--请选择--" clearable>
                <i-option :value="0">非并行</i-option>
                <i-option :value="1">并行</i-option>
              </i-select>
            </form-item>
          </Col>
          <Col span="6">
            <form-item label="时间范围">
              <date-picker type="daterange" placement="bottom-end" placeholder="Select date"
                           :options="dateOptions" v-model="searchDate"/>
            </form-item>
          </Col>

        </Row>
      </Form>
      <i-table class="data-list-table" border highlight-row max-height="620"
               :columns="columns" :data="dataList"
               :loading="loading"></i-table>
      <Page :total="pageArgs.total" :current="pageArgs.pageNum" :page-size="pageArgs.pageSize"
            @on-change="onPageNumChange"
            @on-page-size-change="onPageSizeChange"
            align="right" show-elevator show-total show-sizer
            transfer
            style="margin-top: 20px"
      ></Page>

      <Modal
          v-model="showErrorModal"
          title="执行信息"
          width="60"
          footer-hide
      >
        <pre style="background: rgba(0, 0, 0, 0.05);max-height: 550px;overflow-y: auto">{{ errorMsg }}</pre>
        <!--                        <Text code>{{errorMsg}}</Text>-->
      </Modal>
    </Card>
  </div>
</template>

<script>

const token = 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaW5mbyI6eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJyb2xlSWQiOjEsImdyb3VwSWQiOjEsImZpcm1JZCI6MSwiZmlybSI6bnVsbCwiZGVwYXJ0bWVudElkIjozNywiZGVwYXJ0bWVudENvZGUiOiIxLTM3LSIsImRlcGFydG1lbnROYW1lIjoi5LqR5bmz5Y-wIiwiYXV0aG9yaXR5Q29kZXMiOm51bGwsIm9wZW5JZCI6bnVsbCwidGltZXN0YW1wIjowLCJkZXYiOnRydWUsIm9wcyI6ZmFsc2UsImZpcm1NYW5hZ2VyIjpmYWxzZSwic2lrc2hyIjpmYWxzZSwicGxhdGZvcm1NYW5hZ2VyIjpmYWxzZX0sImlhdCI6MTY3NzQ2NTQ5NCwiZXhwIjoxNzcyMDczNDk0fQ.bHOTmelaRujo37Mqv5oK6MEWpE_NZAN-fJ08lNAgSQzPFOwcw4eHEXat33CYvPRm3O24_mO8HoS46zGxrPNBog';

function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

import 'view-design/dist/styles/iview.css';

import MyText from '@/component/text/my-text.vue'
export default {
  components: {MyText},
  props: {
    instance: { //<1>
      type: Object,
      required: true
    }
  },
  data() {
    return {
      serviceUrl: '',

      condition: {},
      columns: [
        {
          type: 'index',
          width: 60,
          align: 'center'
        },
        {
          title: '任务名',
          key: 'name',
          width: 200,
          resizable: true,
          align: 'center'
        },
        {
          title: '方法名',
          key: 'methodName',
          resizable: true,
          width: 460,
          align: 'center',
          //    <Text code>View Design (code)</Text>
          render: (h, params) => {
            // 0-执行中，1-执行成功，2-执行失败
            let methodName = params.row.methodName;
            return h('div', [
              h(MyText, {
                code: true
              }, methodName),
            ]);
          }
        },
        {
          title: '表达式',
          key: 'expression',
          resizable: true,
          width: 200,
          align: 'center'
        },
        {
          title: '是否并行执行',
          key: 'allowParallel',
          width: 130,
          resizable: true,
          align: 'center',
          render: (h, params) => {
            let allowParallelText = params.row.allowParallel ? '并行' : '非并行';
            return h('div', [
              h('span', {}, allowParallelText),
            ]);
          }
        },
        {
          title: '开始时间',
          key: 'startTime',
          resizable: true,
          width: 200,
          align: 'center'
        },
        {
          title: '结束时间',
          key: 'endTime',
          resizable: true,
          width: 200,
          align: 'center'
        },
        {
          title: '执行实例',
          key: 'instanceId',
          resizable: true,
          width: 180,
          align: 'center'
        },

        {
          title: '执行状态',
          key: 'execStatus',
          width: 150,
          resizable: true,
          align: 'center',
          render: (h, params) => {
            // 0-执行中，1-执行成功，2-执行失败
            let color = ['primary', 'success', 'error'];
            let text = ['执行中', '执行成功', '执行失败'];
            let execStatus = params.row.execStatus;
            return h('Tag', {
              props:{
                color: color[execStatus],
                type: 'border',
              },
              style: {
                cursor: execStatus === 2 ? 'pointer' : '',
              },
              nativeOn: {
                click: () => {
                  this.showErrorMsg(params.row.errorMsg)
                }
              }
            }, text[execStatus]);
          }
        },
      ],
      loading: false,
      dataList: [],
      pageArgs: {
        total: 0,
        pageNum: 1,
        pageSize: 10
      },
      dateOptions: {
        shortcuts: [
          {
            text: '近7天',
            value() {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              return [start, end];
            }
          },
          {
            text: '近15天',
            value() {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 15);
              return [start, end];
            }
          },
          {
            text: '近30天',
            value() {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              return [start, end];
            }
          }
        ]
      },
      searchDate: [],
      errorMsg: '',
      showErrorModal: false
    }
  },
  created() {
    this.serviceUrl = this.instance.registration.serviceUrl;
    this.page();
  },
  methods: {
    async page() {
      const params = {
        args: JSON.stringify(this.condition),
        pageNum: this.pageArgs.pageNum,
        pageSize: this.pageArgs.pageSize
      };
      const token = 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaW5mbyI6eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJyb2xlSWQiOjEsImdyb3VwSWQiOjEsImZpcm1JZCI6MSwiZmlybSI6bnVsbCwiZGVwYXJ0bWVudElkIjozNywiZGVwYXJ0bWVudENvZGUiOiIxLTM3LSIsImRlcGFydG1lbnROYW1lIjoi5LqR5bmz5Y-wIiwiYXV0aG9yaXR5Q29kZXMiOm51bGwsIm9wZW5JZCI6bnVsbCwidGltZXN0YW1wIjowLCJkZXYiOnRydWUsIm9wcyI6ZmFsc2UsImZpcm1NYW5hZ2VyIjpmYWxzZSwic2lrc2hyIjpmYWxzZSwicGxhdGZvcm1NYW5hZ2VyIjpmYWxzZX0sImlhdCI6MTY3NzQ2NTQ5NCwiZXhwIjoxNzcyMDczNDk0fQ.bHOTmelaRujo37Mqv5oK6MEWpE_NZAN-fJ08lNAgSQzPFOwcw4eHEXat33CYvPRm3O24_mO8HoS46zGxrPNBog';
      try {
        const response = await this.instance.axios.get('actuator/taskLog', {params}, {
          headers: {
            'Authorization': token
          }
        }); //<2>
        this.dataList = response.data.list;
        this.pageArgs.pageNum = response.data.pageNum;
        this.pageArgs.pageSize = response.data.pageSize;
        this.pageArgs.total = response.data.total;
      } catch (e) {
        this.$Message.error('查询失败')
      }
    },

    onPageNumChange(pageNum) {
      this.pageArgs.pageNum = pageNum;
      this.page()
    },
    onPageSizeChange(pageSize) {
      this.pageArgs.pageSize = pageSize;
      this.page()
    },
    search() {
      this.pageArgs.pageNum = 1;
      this.pageArgs.total = 0;
      this.page()
    },
    reset() {
      this.condition = {};
      this.searchDate = [];
      this.search();
    },
    pagePost() {
      const url = this.serviceUrl + 'taskLog/findByPage';
      const data = {
        args: JSON.parse(JSON.stringify(this.condition)),
        pageNum: this.pageArgs.pageNum,
        pageSize: this.pageArgs.pageSize
      };
      if (this.searchDate && this.searchDate.length && this.searchDate[0]) {
        data.args.startDate = formatDate(this.searchDate[0]);
        data.args.endDate = formatDate(this.searchDate[1]);
      }
      this.instance.axios.post(url, data, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': token
        }
      })
          .then(response => {
            this.dataList = response.data.data.list;
            this.pageArgs = response.data.data;
          })
          .catch(error => {
            console.error(error);
          });
    },
    showErrorMsg(errorMsg) {
      if (errorMsg) {
        this.errorMsg = errorMsg;
        this.showErrorModal = true;
      }
    }
  },
  computed: {
    iframeUrl() {
      return `/static/task-log.html?serviceUrl=${encodeURIComponent(this.serviceUrl)}`
    }
  },
};
</script>

<style>
.task-log {
//padding: 10px; padding: 3rem 1.5rem;
  width: 100%;
  height: 100%;
}

.search-button {
  text-align: right;
  padding-right: 5px;
}
</style>
