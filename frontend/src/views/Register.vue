<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../api'

const router = useRouter()
const loading = ref(false)
const canvasRef = ref(null)
const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: ''
})

let animationId = null

const initParticles = () => {
  const canvas = canvasRef.value
  if (!canvas) return
  const ctx = canvas.getContext('2d')
  canvas.width = window.innerWidth
  canvas.height = window.innerHeight

  const particles = []
  for (let i = 0; i < 60; i++) {
    particles.push({
      x: Math.random() * canvas.width,
      y: Math.random() * canvas.height,
      size: Math.random() * 3 + 1,
      speedX: (Math.random() - 0.5) * 0.8,
      speedY: (Math.random() - 0.5) * 0.8
    })
  }

  const animate = () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    particles.forEach(p => {
      p.x += p.speedX
      p.y += p.speedY
      if (p.x < 0 || p.x > canvas.width) p.speedX *= -1
      if (p.y < 0 || p.y > canvas.height) p.speedY *= -1
      ctx.beginPath()
      ctx.arc(p.x, p.y, p.size, 0, Math.PI * 2)
      ctx.fillStyle = 'rgba(255, 255, 255, 0.4)'
      ctx.fill()
    })
    animationId = requestAnimationFrame(animate)
  }
  animate()
}

const handleRegister = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请填写完整信息')
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    ElMessage.warning('两次密码不一致')
    return
  }
  loading.value = true
  try {
    await register({
      username: form.value.username,
      password: form.value.password,
      nickname: form.value.nickname
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

onMounted(() => initParticles())
onUnmounted(() => { if (animationId) cancelAnimationFrame(animationId) })
</script>

<template>
  <div class="register-container">
    <canvas ref="canvasRef" class="particles"></canvas>
    <div class="register-card">
      <div class="card-glow"></div>
      <h2 class="title">用户注册</h2>
      <p class="subtitle">Create Your Account</p>
      <el-form :model="form" class="register-form">
        <el-form-item>
          <div class="input-wrapper">
            <el-icon class="input-icon"><User /></el-icon>
            <input v-model="form.username" type="text" placeholder="请输入用户名" class="custom-input" />
          </div>
        </el-form-item>
        <el-form-item>
          <div class="input-wrapper">
            <el-icon class="input-icon"><UserFilled /></el-icon>
            <input v-model="form.nickname" type="text" placeholder="请输入昵称（可选）" class="custom-input" />
          </div>
        </el-form-item>
        <el-form-item>
          <div class="input-wrapper">
            <el-icon class="input-icon"><Lock /></el-icon>
            <input v-model="form.password" type="password" placeholder="请输入密码" class="custom-input" />
          </div>
        </el-form-item>
        <el-form-item>
          <div class="input-wrapper">
            <el-icon class="input-icon"><Lock /></el-icon>
            <input v-model="form.confirmPassword" type="password" placeholder="请确认密码" class="custom-input" />
          </div>
        </el-form-item>
        <el-form-item>
          <button type="button" class="register-btn" :class="{ loading }" @click="handleRegister">
            <span v-if="!loading">注 册</span>
            <span v-else>注册中...</span>
          </button>
        </el-form-item>
      </el-form>
      <div class="footer">
        已有账号？<router-link to="/login">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0c0c1e 0%, #1a1a3e 50%, #0d0d2b 100%);
  overflow: hidden;
  position: relative;
}

.particles {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1;
}

.register-card {
  position: relative;
  z-index: 2;
  width: 420px;
  padding: 50px 40px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.5);
  overflow: hidden;
}

.card-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(118, 75, 162, 0.15) 0%, transparent 70%);
  animation: rotate 10s linear infinite;
  pointer-events: none;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.title {
  text-align: center;
  margin-bottom: 10px;
  color: #fff;
  font-size: 28px;
  font-weight: bold;
  text-shadow: 0 0 20px rgba(118, 75, 162, 0.5);
}

.subtitle {
  text-align: center;
  margin-bottom: 35px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
  letter-spacing: 2px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
}

.input-icon {
  position: absolute;
  left: 15px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 18px;
  z-index: 1;
  transition: color 0.3s;
}

.input-wrapper:focus-within .input-icon {
  color: #764ba2;
}

.custom-input {
  width: 100%;
  padding: 15px 15px 15px 45px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  color: #fff;
  font-size: 15px;
  outline: none;
  transition: all 0.3s;
}

.custom-input::placeholder {
  color: rgba(255, 255, 255, 0.4);
}

.custom-input:focus {
  border-color: #764ba2;
  background: rgba(118, 75, 162, 0.1);
  box-shadow: 0 0 20px rgba(118, 75, 162, 0.2);
}

.register-btn {
  width: 100%;
  padding: 15px;
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
  border: none;
  border-radius: 10px;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(118, 75, 162, 0.4);
}

.register-btn:active {
  transform: translateY(0);
}

.register-btn.loading {
  opacity: 0.7;
  cursor: not-allowed;
}

.footer {
  text-align: center;
  margin-top: 25px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
}

.footer a {
  color: #764ba2;
  text-decoration: none;
  transition: color 0.3s;
}

.footer a:hover {
  color: #9b6fc3;
}

:deep(.el-form-item) {
  margin-bottom: 22px;
}
</style>