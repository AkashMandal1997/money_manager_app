# 🚀 Deploying to Render with Docker

## Quick Deploy Guide

### 1. **Push to GitHub** (DO NOT include target/)
```bash
git add .
git commit -m "Add multi-stage Dockerfile for Render deployment"
git push origin main
```

✅ The `target/` folder is already in `.gitignore` - this is correct!

### 2. **Create Render Web Service**

1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub repository: `AkashMandal1997/money_manager_app`
4. Configure the service:

   ```
   Name: money-manager-backend
   Region: Choose nearest to you
   Branch: main
   Root Directory: moneymanager
   Environment: Docker
   Instance Type: Free (or paid for better performance)
   ```

### 3. **Environment Variables**

Add these in Render dashboard under "Environment":

```
POSTGRES_URL=<your_render_postgres_url>
POSTGRES_USERNAME=<your_postgres_user>
POSTGRES_PASSWORD=<your_postgres_password>
BREVO_USERNAME=<your_brevo_username>
BREVO_PASSWORD=<your_brevo_api_key>
BREVO_FROM_EMAIL=<your_verified_email>
SPRING_PROFILES_ACTIVE=prod
```

### 4. **Create PostgreSQL Database** (if needed)

1. In Render Dashboard: **"New +"** → **"PostgreSQL"**
2. Copy the **Internal Database URL**
3. Use it as `POSTGRES_URL` in your web service

### 5. **Deploy**

Click **"Create Web Service"** - Render will:
- ✅ Pull your code from GitHub
- ✅ Build using the multi-stage Dockerfile
- ✅ Compile your Java code with Maven
- ✅ Create the JAR file
- ✅ Deploy the application

### 6. **Update Frontend**

Once deployed, update your frontend `apiEndpoints.js`:

```javascript
export const BASE_URL = "https://your-service-name.onrender.com/api/v1.0";
```

## 📋 Why Multi-Stage Dockerfile?

**Old Dockerfile (❌ Won't work on Render):**
```dockerfile
FROM eclipse-temurin:21-jre
COPY target/moneymanager-0.0.1-SNAPSHOT.jar app.jar
```
- Expects JAR file to exist
- But `target/` is not in GitHub (correctly ignored)
- Build will fail!

**New Multi-Stage Dockerfile (✅ Works on Render):**
```dockerfile
# Stage 1: Build
FROM eclipse-temurin:21-jdk AS build
COPY pom.xml .
COPY src src
RUN ./mvnw clean package

# Stage 2: Runtime
FROM eclipse-temurin:21-jre
COPY --from=build /app/target/*.jar app.jar
```
- Builds code during Docker build
- No need to commit JAR files
- Optimized image size (JRE vs JDK)

## 🎯 Dockerfile Features

✅ **Multi-stage build** - Compiles code in Docker
✅ **Layer caching** - Dependencies cached separately
✅ **Optimized size** - Uses JRE for runtime
✅ **Render compatible** - Respects PORT environment variable
✅ **Production ready** - Skips tests during build

## 🔧 Local Testing

Test the Docker build locally:

```bash
cd moneymanager

# Build the image
docker build -t money-manager .

# Run the container
docker run -p 8080:8080 \
  -e POSTGRES_URL=your_db_url \
  -e POSTGRES_USERNAME=your_user \
  -e POSTGRES_PASSWORD=your_pass \
  -e BREVO_USERNAME=your_brevo_user \
  -e BREVO_PASSWORD=your_brevo_key \
  -e BREVO_FROM_EMAIL=your_email \
  money-manager
```

## 📊 Deployment Checklist

- [x] Dockerfile uses multi-stage build
- [x] `.dockerignore` created
- [x] `target/` in `.gitignore` (not pushed to GitHub)
- [ ] Code pushed to GitHub
- [ ] Render web service created
- [ ] PostgreSQL database created
- [ ] Environment variables configured
- [ ] Application deployed and running
- [ ] Frontend BASE_URL updated

## 🚨 Important Notes

1. **Never commit target/ folder** - It's build artifacts
2. **Never commit .env files** - They contain secrets
3. **Use Render's PostgreSQL** - It's integrated and free tier available
4. **Monitor logs** - Check Render logs for any startup issues
5. **Cold starts** - Free tier sleeps after inactivity

## 🔗 Useful Links

- [Render Dashboard](https://dashboard.render.com/)
- [Render Docs - Deploy with Docker](https://render.com/docs/docker)
- [Render Docs - Environment Variables](https://render.com/docs/environment-variables)

---

**Ready to deploy!** Push your code and create the Render service. 🚀
