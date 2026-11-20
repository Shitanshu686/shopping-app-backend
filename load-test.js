import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const AUTH_TOKEN = __ENV.AUTH_TOKEN || '';

const errorRate = new Rate('errors');
const productsLatency = new Trend('products_latency');
const cartLatency = new Trend('cart_latency');

export const options = {
  stages: [
    { duration: '30s', target: 100 },    // Warm-up
    { duration: '1m', target: 1000 },    // 1,000 VUs load
    { duration: '1m', target: 3000 },    // 3,000 VUs ramp
    { duration: '1m', target: 7000 },    // 7,000 VUs ramp
    { duration: '1m', target: 10000 },   // 10,000 VUs peak load
    { duration: '30s', target: 0 },      // Scale down
  ],
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<500', 'p(99)<1200'],
    products_latency: ['p(95)<500'],
    cart_latency: ['p(95)<700'],
  },
};

export default function () {
  const headers = {
    'Content-Type': 'application/json',
    ...(AUTH_TOKEN ? { Authorization: `Bearer ${AUTH_TOKEN}` } : {}),
  };

  group('Products Endpoint', () => {
    const res = http.get(`${BASE_URL}/products`, { headers });
    productsLatency.add(res.timings.duration);

    const ok = check(res, {
      'GET /products status is 200': (r) => r.status === 200,
    });
    errorRate.add(!ok);
  });

  sleep(0.5);

  group('Cart Endpoint', () => {
    const res = http.get(`${BASE_URL}/cart`, { headers });
    cartLatency.add(res.timings.duration);

    const ok = check(res, {
      'GET /cart status is 200 or 401': (r) => r.status === 200 || r.status === 401,
    });
    errorRate.add(!ok);
  });

  sleep(1);
}
