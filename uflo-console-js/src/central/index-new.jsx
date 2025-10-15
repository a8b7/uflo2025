/**
 * 新的Central入口文件 - 使用Zustand和Signals
 * Created for UFLO2 migration to modern state management
 */
import '../../node_modules/bootstrap/dist/css/bootstrap.css';

import React from 'react';
import ReactDOM from 'react-dom';
import { ReactSignalsProvider } from '@preact/signals-react';

// 导入新的状态管理
import { useCentralStore, loadingState, errorState, loadingActions, errorActions } from '../stores';

// 导入组件
import ExampleCentralPage from '../examples/ExampleCentralPage';

// 全局错误处理
window.addEventListener('error', (event) => {
  errorActions.showError(event.error);
});

// 网络请求错误处理
window.addEventListener('unhandledrejection', (event) => {
  errorActions.showError(event.reason);
});

// 主应用组件
const App = () => {
  // 初始化数据加载
  const { loadProcesses } = useCentralStore();

  React.useEffect(() => {
    // 加载初始数据
    loadProcesses(1, 10);
  }, [loadProcesses]);

  return (
    <div className="container-fluid">
      <div className="row">
        <div className="col-12">
          <h1 className="mb-4">UFLO2 流程管理中心</h1>
        </div>
      </div>

      <ExampleCentralPage />

      {/* 全局错误显示 */}
      {errorState.value.hasError && (
        <div className="alert alert-danger fixed-bottom m-3" style={{ zIndex: 9999 }}>
          <strong>错误:</strong> {errorState.value.errorMessage}
          <button
            className="close"
            onClick={() => errorActions.clearError()}
          >
            <span>&times;</span>
          </button>
        </div>
      )}
    </div>
  );
};

// 渲染应用
$(document).ready(function() {
  ReactDOM.render(
    <ReactSignalsProvider>
      <App />
    </ReactSignalsProvider>,
    document.getElementById("container")
  );
});