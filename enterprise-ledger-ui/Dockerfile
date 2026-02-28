# STAGE 1: Build the Vue application
FROM node:20-alpine AS builder
WORKDIR /app

# Copy package files and install dependencies cleanly
COPY package*.json ./
RUN npm ci

# Copy the rest of the code and build the production assets
COPY . .
RUN npm run build

# STAGE 2: Serve the application with Nginx
FROM nginx:alpine

# Copy the compiled 'dist' folder from Stage 1 into Nginx's public folder
COPY --from=builder /app/dist /usr/share/nginx/html

# Expose port 80 (Internal Docker port)
EXPOSE 80

# Start Nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]