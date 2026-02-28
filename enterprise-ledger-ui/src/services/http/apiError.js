class ApiError extends Error {
  constructor(message, status = null, code = null, details = null) {
    super(message);
    this.status = status;
    this.code = code;
    this.details = details;
  }

  static fromAxios(error) {
    if (error.response) {
      return new ApiError(
        error.response.data?.message || 'Server error',
        error.response.status,
        error.response.data?.code,
        error.response.data
      );
    }

    if (error.request) {
      return new ApiError('Network error. Please check your connection.');
    }

    return new ApiError(error.message);
  }
}

export default ApiError;
