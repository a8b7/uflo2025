module.exports = {
  // 测试环境
  testEnvironment: 'jsdom',

  // 测试文件匹配模式
  testMatch: [
    '**/tests/**/*.test.(js|jsx|ts|tsx)',
    '**/__tests__/**/*.(js|jsx|ts|tsx)'
  ],

  // 转换配置
  transform: {
    '^.+\\.(js|jsx)$': 'babel-jest',
    '^.+\\.(ts|tsx)$': 'ts-jest'
  },

  // 模块文件扩展名
  moduleFileExtensions: ['js', 'jsx', 'ts', 'tsx', 'json'],

  // 模块名称映射
  moduleNameMapping: {
    '^@/(.*)$': '<rootDir>/src/$1',
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy'
  },

  // 设置文件
  setupFilesAfterEnv: [
    '<rootDir>/src/tests/setupTests.ts'
  ],

  // 覆盖率收集
  collectCoverage: true,
  collectCoverageFrom: [
    'src/**/*.{js,jsx,ts,tsx}',
    '!src/**/*.d.ts',
    '!src/tests/**',
    '!src/examples/**'
  ],
  coverageDirectory: 'coverage',
  coverageReporters: ['text', 'lcov', 'html'],

  // 忽略的路径
  testPathIgnorePatterns: [
    '/node_modules/',
    '/dist/',
    '/build/'
  ],

  // 转换忽略模式
  transformIgnorePatterns: [
    'node_modules/(?!(axios)/)'
  ],

  // 清除模拟
  clearMocks: true,

  // 详细输出
  verbose: true
};