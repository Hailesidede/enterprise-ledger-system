import axios from 'axios';
import { attachInterceptors } from './interceptors';
import ApiError from './apiError';
import { env } from '../http/config/env';

class HttpClient {
  constructor() {
    this.instance = axios.create({
      baseURL: env.API_BASE_URL,
      timeout: 30000,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    attachInterceptors(this.instance);
  }

  async get(url, config = {}) {
    return this._handle(this.instance.get(url, config));
  }

  async post(url, data = {}, config = {}) {
    return this._handle(this.instance.post(url, data, config));
  }

  async put(url, data = {}, config = {}) {
    return this._handle(this.instance.put(url, data, config));
  }

  async delete(url, config = {}) {
    return this._handle(this.instance.delete(url, config));
  }

  async _handle(promise) {
    try {
      const response = await promise;
      console.log('response we got::', response);
      return response.data;
    } catch (error) {
      console.log('error', error);
      throw ApiError.fromAxios(error);
    }
  }
}

export default HttpClient;
