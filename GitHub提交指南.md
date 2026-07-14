# GitHub提交指南

## 一、初始设置

### 1. 创建GitHub仓库
1. 登录GitHub，点击右上角"+" -> "New repository"
2. 仓库名：`ds-system`
3. 描述：`智能仓储管理系统 - SpringBoot3 + Vue3`
4. 选择 Public（公开）
5. 勾选 "Add a README file"
6. 点击 "Create repository"

### 2. 克隆仓库到本地
```bash
# 打开终端，进入你的项目目录
cd "D:\vscode project"

# 克隆仓库
git clone https://github.com/你的用户名/ds-system.git

# 进入项目目录
cd ds-system
```

## 二、Git配置（首次使用）

```bash
# 设置用户名（GitHub用户名）
git config --global user.name "你的用户名"

# 设置邮箱（GitHub注册邮箱）
git config --global user.email "你的邮箱@example.com"

# 验证配置
git config --list
```

## 三、分支管理策略

### 创建开发分支
```bash
# 从main分支创建develop分支
git checkout -b develop

# 推送到远程
git push -u origin develop
```

### 功能开发分支命名规则
```
feature/user-auth      # 用户认证功能
feature/product        # 商品管理功能
feature/order          # 订单管理功能
feature/seckill        # 秒杀功能
feature/inventory      # 库存管理功能
```

## 四、提交规范

### Commit Message格式
```
<type>: <subject>

<body>

<footer>
```

### Type类型说明
| 类型 | 说明 | 示例 |
|------|------|------|
| feat | 新功能 | feat: 添加用户登录API |
| fix | 修复bug | fix: 修复商品库存扣减bug |
| style | 代码格式（不影响逻辑） | style: 调整页面样式 |
| docs | 文档 | docs: 更新README |
| refactor | 重构 | refactor: 重构订单服务 |
| test | 测试 | test: 添加用户模块测试 |
| chore | 构建/工具 | chore: 配置Docker环境 |

### 提交频率建议
- ✅ 完成一个独立功能点后提交
- ✅ 修复一个bug后提交
- ✅ 完成一个阶段后提交
- ❌ 不要堆积大量代码一次提交
- ❌ 不要提交 "update" 或 "fix bug" 这种无意义的commit

## 五、实际提交流程

### 场景1：完成用户登录功能
```bash
# 1. 切换到功能分支
git checkout -b feature/user-auth

# 2. 查看修改的文件
git status

# 3. 添加所有修改
git add .

# 4. 提交（写清楚做了什么）
git commit -m "feat: 实现用户登录功能

- 添加User实体类和Mapper
- 实现JWT工具类
- 配置Spring Security
- 添加登录/注册API接口
- 实现密码加密存储"

# 5. 推送到远程
git push -u origin feature/user-auth
```

### 场景2：修复登录bug
```bash
# 1. 查看修改
git status

# 2. 添加修改
git add backend/src/main/java/com/ds/security/JwtTokenProvider.java

# 3. 提交
git commit -m "fix: 修复JWT Token过期时间设置错误

- 修复Token过期时间计算错误
- 添加Token刷新机制"

# 4. 推送
git push
```

### 场景3：阶段完成合并
```bash
# 1. 切换到develop分支
git checkout develop

# 2. 合并功能分支
git merge feature/user-auth

# 3. 推送develop分支
git push origin develop

# 4. 删除已合并的功能分支（可选）
git branch -d feature/user-auth
git push origin --delete feature/user-auth
```

## 六、每次提交必须包含的信息

### 提交信息模板
```
feat: 简短描述

详细描述（1-5行）：
- 做了什么
- 为什么这么做
- 影响范围

相关Issue: #1（如果有）
```

### 实际示例
```
feat: 实现Redis商品缓存

实现商品列表和详情的Redis缓存：
- 添加Redis配置类
- 实现商品查询缓存逻辑
- 添加缓存过期时间设置（30分钟）
- 实现缓存更新策略

技术要点：
- 使用Spring Data Redis
- 采用@Cacheable注解
- 缓存击穿保护：互斥锁

相关功能: 商品管理模块
```

## 七、验证提交是否成功

### 检查提交历史
```bash
# 查看最近10条提交
git log --oneline -10

# 查看详细提交信息
git log -5

# 查看提交统计
git shortlog -sn
```

### 在GitHub上验证
1. 打开你的GitHub仓库页面
2. 点击 "Commits" 查看提交历史
3. 检查每个commit的message是否清晰

## 八、常见错误处理

### 1. 提交了错误的文件
```bash
# 撤销上次提交（保留修改）
git reset --soft HEAD~1

# 重新添加正确的文件
git add .
git commit -m "正确的commit message"
```

### 2. 忘记添加某些文件
```bash
# 添加遗漏的文件
git add 遗漏的文件路径

# 修改上次提交（不推荐多次修改）
git commit --amend -m "更新的commit message"
```

### 3. 冲突解决
```bash
# 如果合并时出现冲突
# 1. 打开冲突文件，手动解决
# 2. 添加解决后的文件
git add 冲突文件

# 3. 继续合并
git commit -m "解决合并冲突"
```

## 九、考核要求对照

根据考核方案，你需要确保：

1. ✅ **提交频率**：每完成一个功能点就提交
2. ✅ **Commit Message**：清晰描述做了什么，不能是"update"
3. ✅ **提交内容**：每次提交有实际的代码变更
4. ✅ **分支管理**：使用功能分支开发
5. ✅ **代码结构**：项目目录清晰规范

## 十、提交检查清单

每次提交前，检查：
- [ ] commit message是否清晰描述了改动
- [ ] 是否添加了所有相关文件
- [ ] 是否排除了临时文件和敏感信息
- [ ] 代码是否能正常运行
- [ ] 是否符合项目代码规范