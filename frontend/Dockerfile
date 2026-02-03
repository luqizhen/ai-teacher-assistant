# Use nginx as the base image
FROM nginx:alpine

# Remove default nginx configuration and files
RUN rm /etc/nginx/conf.d/default.conf && \
    rm -rf /usr/share/nginx/html/*

# Copy static files
COPY src/ /usr/share/nginx/html/

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost/ || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
