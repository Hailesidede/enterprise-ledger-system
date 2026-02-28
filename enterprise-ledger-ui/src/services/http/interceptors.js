export function attachInterceptors(instance) {
  instance.interceptors.request.use(config => {
    const token = localStorage.getItem('access_token');

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  });

  instance.interceptors.response.use(
    response => response,
    async error => {
      if (error.response?.status === 401) {
        // trigger a refresh of token flow
      }

      return Promise.reject(error);
    }
  );
}
