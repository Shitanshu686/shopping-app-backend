import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 10 },   // Stage 1: 10 users baseline
    { duration: '1m', target: 100 },   // Stage 2: 100 users moderate load
    { duration: '1m', target: 1000 },  // Stage 3: 1,000 users high load
    { duration: '30s', target: 0 },    // Scale down
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],  // 95% of requests should be below 500ms
  },
};

export default function () {
  // Hit a public health or product catalog endpoint in your ShopEase backend
  const res = http.get('http://localhost:8080/api/products');
  
  check(res, {
    'status is 200': (r) => r.status === 200,
  });
  
  sleep(1);
}
