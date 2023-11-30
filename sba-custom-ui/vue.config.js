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

module.exports = {
  // outputDir: 'target/dist',
  outputDir: 'target/classes/META-INF/spring-boot-admin-server-ui/extensions/custom',
  chainWebpack: config => {
    config.entryPoints.delete('app');
    config.entry('custom').add('./src/index.js');
    /*config.externals({
      vue: {
        commonjs: 'vue',
        commonjs2: 'vue',
        root: 'Vue'
      }
    });*/
    if (process.env.NODE_ENV === 'development') {
      //Fix different paths for watch-mode
      config.output.filename('js/[name].js');
      config.output.chunkFilename('js/[name].js');
    }
    config.output.libraryTarget('var');
    config.optimization.splitChunks(false);
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => ({
        ...options,
        hotReload: false
      }));
    // 配置font字体无论多大，都打成base64，否则iview font加载不了
    config.module.rule('fonts').use('url-loader')
        .tap(options => ({
          name: 'fonts/[name].[hash:8].[ext]',
          limit: 0
        }))
    config.plugins.delete('html');
    config.plugins.delete('preload');
    config.plugins.delete('prefetch');
  }
};
