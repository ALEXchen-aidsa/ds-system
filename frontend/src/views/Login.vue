<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api'

const router = useRouter()
const loading = ref(false)
const canvasRef = ref(null)
const form = ref({
  username: '',
  password: ''
})

let animationId = null

const initParticles = () => {
  const canvas = canvasRef.value
  if (!canvas) return
  const ctx = canvas.getContext('2d')
  canvas.width = window.innerWidth
  canvas.height = window.innerHeight

  const particles = []
  const particleCount = 80

  for (let i = 0; i < particleCount; i++) {
    particles.push({
      x: Math.random() * canvas.width,
      y: Math.random() * canvas.height,
      vx: (Math.random() - 0.5) * 0.5,
      vy: (Math.random() - 0.5) * 0.5,
      size: Math.random() * 2 + 1
    })
  }

  const animate = () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    
    particles.forEach((p, i) => {
      p.x += p.vx
      p.y += p.vy

      if (p.x < 0 || p.x > canvas.width) p.vx *= -1
      if (p.y < 0 || p.y > canvas.height) p.vy *= -1

      ctx.beginPath()
      ctx.arc(p.x, p.y, p.size, 0, Math.PI * 2)
      ctx.fillStyle = 'rgba(255, 255, 255, 0.6)'
      ctx.fill()

      particles.forEach((p2, j) => {
        if (i === j) return
        const dx = p.x - p2.x
        const dy = p.y - p2.y
        const dist = Math.sqrt(dx * dx + dy * dy)
        if (dist < 150) {
          ctx.beginPath()
          ctx.moveTo(p.x, p.y)
          ctx.lineTo(p2.x, p2.y)
          ctx.strokeStyle = `rgba(255, 255, 255, ${0.3 * (1 - dist / 150)})`
          ctx.stroke()
        }
      })
    })

    animationId = requestAnimationFrame(animate)
  }

  animate()
}

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await login(form.value)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('username', res.data.username)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  initParticles()
  window.addEventListener('resize', () => {
    if (canvasRef.value) {
      canvasRef.value.width = window.innerWidth
      canvasRef.value.height = window.innerHeight
    }
  })
})

onUnmounted(() => {
  if (animationId) cancelAnimationFrame(animationId)
})
</script>

<template>
  <div class="login-container">
    <canvas ref="canvasRef" class="particles"></canvas>
    <div class="login-card">
      <div class="card-glow"></div>
      <h2 class="title">智能仓储系统</h2>
      <p class="subtitle">Smart Warehouse Management</p>
      <el-form :model="form" class="login-form">
        <el-form-item>
          <div class="input-wrapper">
            <el-icon class="input-icon"><User /></el-icon>
            <input 
              v-model="form.username" 
              type="text"
              placeholder="请输入用户名"
              class="custom-input"
            />
          </div>
        </el-form-item>
        <el-form-item>
          <div class="input-wrapper">
            <el-icon class="input-icon"><Lock /></el-icon>
            <input 
              v-model="form.password" 
              type="password"
              placeholder="请输入密码"
              class="custom-input"
              @keyup.enter="handleLogin"
            />
          </div>
        </el-form-item>
        <el-form-item>
          <button 
            type="button"
            class="login-btn"
            :class="{ loading }"
            @click="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登录中...</span>
          </button>
        </el-form-item>
      </el-form>
      <div class="footer">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
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

.login-card {
  position: relative;
  z-index: 2;
  width: 420px;
  padding: 50px 40px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.5),
              0 0 100px rgba(100, 100, 255, 0.1);
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 30px 60px rgba(0, 0, 0, 0.5),
              0 0 120px rgba(100, 100, 255, 0.15);
}

.card-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(100, 100, 255, 0.1) 0%, transparent 70%);
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
  text-shadow: 0 0 20px rgba(100, 150, 255, 0.5);
}

.subtitle {
  text-align: center;
  margin-bottom: 40px;
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
  color: #667eea;
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
  border-color: #667eea;
  background: rgba(100, 100, 255, 0.1);
  box-shadow: 0 0 20px rgba(100, 100, 255, 0.2);
}

.login-btn {
  width: 100%;
  padding: 15px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 10px;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
}

.login-btn:active {
  transform: translateY(0);
}

.login-btn.loading {
  opacity: 0.7;
  cursor: not-allowed;
}

.login-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.5s;
}

.login-btn:hover::before {
  left: 100%;
}

.footer {
  text-align: center;
  margin-top: 25px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
}

.footer a {
  color: #667eea;
  text-decoration: none;
  transition: color 0.3s;
}

.footer a:hover {
  color: #8b9cf7;
  text-shadow: 0 0 10px rgba(102, 126, 234, 0.5);
}

:deep(.el-form-item) {
  margin-bottom: 25px;
}
</style>