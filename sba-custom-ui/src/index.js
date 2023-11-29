/*
 * Copyright 2014-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* global SBA */
import custom from './custom/custom';
import customEndpoint from './custom/custom-endpoint';
import threadpoolEndpoint from './threadpool/threadpool-endpoint.vue';
import taskLogEndpoint from './task-log/task-log-endpoint.vue';
import demoEndpoint from './demo/demo-endpoint.vue';

// import {Button} from 'element-ui';
// import ElementUI from 'element-ui';

const installElementUI = (vue) => {
  if (window.Vue) {
    return
  }
  // const echarts = () => import('echarts');
  // console.log('init echarts 2', echarts)
  // window.echarts = echarts
  window.Vue = window.vue = vue
  const ElementUI = () => import('element-ui');
  import('element-ui/lib/theme-chalk/index.css');
  vue.use(ElementUI);
}

// tag::customization-ui-toplevel[]
SBA.use({
  async install({viewRegistry, vue}) {

    // vue.use(Button)
    // const Ant = () => import('ant-design-vue');
    // vue.use(Ant);
    installElementUI(vue);
    viewRegistry.addView({
      name: 'custom',  //<1>
      path: '/custom', //<2>
      component: custom, //<3>
      label: 'Custom', //<4>
      order: 1000, //<5>
    });
  }
});
// end::customization-ui-toplevel[]

// tag::customization-ui-endpoint[]
SBA.use({
  async install({viewRegistry, vue}) {
    installElementUI(vue);
    viewRegistry.addView({
      name: 'instances/custom',
      parent: 'instances', // <1>
      path: 'custom',
      component: customEndpoint,
      label: 'Custom',
      order: 1000,
      isEnabled: ({instance}) => instance.hasEndpoint('custom') // <2>
    });
  }
});
// end::customization-ui-endpoint[]
SBA.use({
  async install({viewRegistry, vue}) {
    installElementUI(vue);
    viewRegistry.addView({
      name: 'instances/threadPool',
      parent: 'instances', // <1>
      path: 'threadPool',
      component: threadpoolEndpoint,
      label: 'ThreadPool',
      order: 1000,
      isEnabled: ({instance}) => instance.hasEndpoint('threadPool') // <2>
    });
  }
});


SBA.use({
  async install({viewRegistry, vue}) {
    installElementUI(vue);
    viewRegistry.addView({
      name: 'instances/taskLog',
      parent: 'instances', // <1>
      path: 'taskLog',
      component: taskLogEndpoint,
      label: 'TaskLog',
      order: 1000,
      isEnabled: ({instance}) => instance.hasEndpoint('taskLog') // <2>
    });
  }
});


// SBA.use({
//   async install({viewRegistry, vue}) {
//     installElementUI(vue);
//     viewRegistry.addView({
//       name: 'instances/demo',
//       parent: 'instances', // <1>
//       path: 'demo',
//       component: demoEndpoint,
//       label: 'Demo',
//       order: 1000,
//       isEnabled: ({instance}) => instance.hasEndpoint('threadPool') // <2>
//     });
//   }
// });
