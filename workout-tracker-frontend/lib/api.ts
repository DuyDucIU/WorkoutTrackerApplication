import axios from 'axios';
import { toast } from 'sonner';

// MOCK MODE - Set to true to test without backend
const MOCK_MODE = false;

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to attach JWT token
api.interceptors.request.use(
  (config) => {
    if (typeof window !== 'undefined') {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      if (typeof window !== 'undefined') {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        toast.error('Session expired, please login again');
        window.location.href = '/login';
      }
    } else if (error.response?.data?.message) {
      toast.error(error.response.data.message);
    } else if (error.response?.data?.errors) {
      const errors = error.response.data.errors;
      Object.values(errors).forEach((msg) => {
        toast.error(msg as string);
      });
    }
    return Promise.reject(error);
  }
);

export default api;

// ============ MOCK DATA ============
const mockExercises: Exercise[] = [
  { id: 1, name: 'Bench Press', category: 'Chest' },
  { id: 2, name: 'Squat', category: 'Legs' },
  { id: 3, name: 'Deadlift', category: 'Back' },
  { id: 4, name: 'Overhead Press', category: 'Shoulders' },
  { id: 5, name: 'Barbell Row', category: 'Back' },
  { id: 6, name: 'Pull-ups', category: 'Back' },
  { id: 7, name: 'Lunges', category: 'Legs' },
  { id: 8, name: 'Bicep Curls', category: 'Arms' },
  { id: 9, name: 'Tricep Dips', category: 'Arms' },
  { id: 10, name: 'Plank', category: 'Core' },
  { id: 11, name: 'Leg Press', category: 'Legs' },
  { id: 12, name: 'Lat Pulldown', category: 'Back' },
];

let mockPlans: WorkoutPlan[] = [
  { id: 1, name: 'Push Day', description: 'Chest, shoulders, and triceps workout', userId: 1, createdAt: '2024-01-15', updatedAt: '2024-01-15' },
  { id: 2, name: 'Pull Day', description: 'Back and biceps focused training', userId: 1, createdAt: '2024-01-16', updatedAt: '2024-01-16' },
  { id: 3, name: 'Leg Day', description: 'Lower body strength and conditioning', userId: 1, createdAt: '2024-01-17', updatedAt: '2024-01-17' },
];

let mockSessions: WorkoutSession[] = [
  {
    id: 1, name: 'Morning Push', notes: 'Felt strong today', workoutDate: '2024-01-20', status: 'COMPLETED', workoutPlanId: 1,
    sessionExercises: [
      { id: 1, exerciseId: 1, exercise: mockExercises[0], sets: 4, reps: 8, weight: 135, orderIndex: 0 },
      { id: 2, exerciseId: 4, exercise: mockExercises[3], sets: 3, reps: 10, weight: 95, orderIndex: 1 },
    ]
  },
  {
    id: 2, name: 'Evening Push', notes: '', workoutDate: '2024-01-22', status: 'PENDING', workoutPlanId: 1,
    sessionExercises: []
  },
  {
    id: 3, name: 'Back Workout', notes: 'Focus on form', workoutDate: '2024-01-21', status: 'COMPLETED', workoutPlanId: 2,
    sessionExercises: [
      { id: 3, exerciseId: 3, exercise: mockExercises[2], sets: 5, reps: 5, weight: 225, orderIndex: 0 },
      { id: 4, exerciseId: 5, exercise: mockExercises[4], sets: 4, reps: 8, weight: 135, orderIndex: 1 },
    ]
  },
  {
    id: 4, name: 'Leg Session', notes: 'Skipped due to injury', workoutDate: '2024-01-19', status: 'SKIPPED', workoutPlanId: 3,
    sessionExercises: []
  },
];

let nextPlanId = 4;
let nextSessionId = 5;
let nextExerciseId = 5;

const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

// Mock response wrapper
const mockResponse = <T>(data: T) => ({ data });

// ============ AUTH API ============
export const authApi = {
  login: async (data: { username: string; password: string }) => {
    if (MOCK_MODE) {
      await delay(500);
      // Accept any credentials in mock mode
      if (data.username && data.password) {
        return mockResponse({
          token: 'mock-jwt-token-' + Date.now(),
          user: { id: 1, fullName: 'Test User', username: data.username, email: data.username + '@test.com' }
        });
      }
      throw { response: { data: { message: 'Invalid credentials' } } };
    }
    return api.post<AuthResponse>('/auth/login', data);
  },
  register: async (data: { fullName: string; username: string; email: string; password: string }) => {
    if (MOCK_MODE) {
      await delay(500);
      return mockResponse({
        token: 'mock-jwt-token-' + Date.now(),
        user: { id: 1, fullName: data.fullName, username: data.username, email: data.email }
      });
    }
    return api.post('/auth/register', data);
  },
};

// ============ WORKOUT PLANS API ============
export const workoutPlansApi = {
  getAll: async () => {
    if (MOCK_MODE) {
      await delay(300);
      return mockResponse(mockPlans);
    }
    return api.get('/workout-plans');
  },
  getById: async (id: number) => {
    if (MOCK_MODE) {
      await delay(200);
      const plan = mockPlans.find(p => p.id === id);
      if (plan) return mockResponse(plan);
      throw { response: { status: 404, data: { message: 'Plan not found' } } };
    }
    return api.get(`/workout-plans/${id}`);
  },
  create: async (data: { name: string; description?: string }) => {
    if (MOCK_MODE) {
      await delay(300);
      const newPlan: WorkoutPlan = {
        id: nextPlanId++,
        name: data.name,
        description: data.description,
        userId: 1,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };
      mockPlans.push(newPlan);
      return mockResponse(newPlan);
    }
    return api.post('/workout-plans', data);
  },
  update: async (id: number, data: { name: string; description?: string }) => {
    if (MOCK_MODE) {
      await delay(300);
      const index = mockPlans.findIndex(p => p.id === id);
      if (index !== -1) {
        mockPlans[index] = { ...mockPlans[index], ...data, updatedAt: new Date().toISOString() };
        return mockResponse(mockPlans[index]);
      }
      throw { response: { status: 404, data: { message: 'Plan not found' } } };
    }
    return api.put(`/workout-plans/${id}`, data);
  },
  delete: async (id: number) => {
    if (MOCK_MODE) {
      await delay(300);
      mockPlans = mockPlans.filter(p => p.id !== id);
      mockSessions = mockSessions.filter(s => s.workoutPlanId !== id);
      return mockResponse({ success: true });
    }
    return api.delete(`/workout-plans/${id}`);
  },
  deleteAll: async () => {
    if (MOCK_MODE) {
      await delay(300);
      mockPlans = [];
      mockSessions = [];
      return mockResponse({ success: true });
    }
    return api.delete('/workout-plans');
  },
};

// ============ WORKOUT SESSIONS API ============
export const workoutSessionsApi = {
  getByPlanId: async (planId: number) => {
    if (MOCK_MODE) {
      await delay(300);
      return mockResponse(mockSessions.filter(s => s.workoutPlanId === planId));
    }
    return api.get(`/workout-plans/${planId}/sessions`);
  },
  create: async (data: WorkoutSessionInput) => {
    if (MOCK_MODE) {
      await delay(300);
      const newSession: WorkoutSession = {
        id: nextSessionId++,
        name: data.name,
        notes: data.notes,
        workoutDate: data.workoutDate,
        status: data.status,
        workoutPlanId: data.workoutPlanId,
        sessionExercises: data.sessionExercises.map((se, idx) => ({
          id: nextExerciseId++,
          exerciseId: se.exerciseId,
          exercise: mockExercises.find(e => e.id === se.exerciseId)!,
          sets: se.sets,
          reps: se.reps,
          weight: se.weight,
          duration: se.duration,
          orderIndex: idx,
        })),
      };
      mockSessions.push(newSession);
      return mockResponse(newSession);
    }
    return api.post('/sessions', data);
  },
  update: async (id: number, data: WorkoutSessionInput) => {
    if (MOCK_MODE) {
      await delay(300);
      const index = mockSessions.findIndex(s => s.id === id);
      if (index !== -1) {
        mockSessions[index] = {
          ...mockSessions[index],
          name: data.name,
          notes: data.notes,
          workoutDate: data.workoutDate,
          status: data.status,
          sessionExercises: data.sessionExercises.map((se, idx) => ({
            id: nextExerciseId++,
            exerciseId: se.exerciseId,
            exercise: mockExercises.find(e => e.id === se.exerciseId)!,
            sets: se.sets,
            reps: se.reps,
            weight: se.weight,
            duration: se.duration,
            orderIndex: idx,
          })),
        };
        return mockResponse(mockSessions[index]);
      }
      throw { response: { status: 404, data: { message: 'Session not found' } } };
    }
    return api.put(`/sessions/${id}`, data);
  },
  delete: async (id: number) => {
    if (MOCK_MODE) {
      await delay(300);
      mockSessions = mockSessions.filter(s => s.id !== id);
      return mockResponse({ success: true });
    }
    return api.delete(`/sessions/${id}`);
  },
};

// ============ EXERCISES API ============
export const exercisesApi = {
  getAll: async () => {
    if (MOCK_MODE) {
      await delay(200);
      return mockResponse(mockExercises);
    }
    return api.get('/exercises');
  },
};

// Types
export interface User {
  id: number;
  fullName: string;
  username: string;
  email: string;
}

export interface AuthResponse {
  token: string;
  authenticated: boolean;
  user: User;
}

export interface WorkoutPlan {
  id: number;
  name: string;
  description?: string;
  userId: number;
  createdAt: string;
  updatedAt: string;
}

export interface WorkoutSession {
  id: number;
  name: string;
  notes?: string;
  workoutDate: string;
  status: 'PENDING' | 'COMPLETED' | 'SKIPPED';
  workoutPlanId: number;
  sessionExercises: SessionExercise[];
}

export interface WorkoutSessionInput {
  name: string;
  notes?: string;
  workoutDate: string;
  status: 'PENDING' | 'COMPLETED' | 'SKIPPED';
  workoutPlanId: number;
  sessionExercises: SessionExerciseInput[];
}

export interface Exercise {
  id: number;
  name: string;
  category: string;
}

export interface SessionExercise {
  id: number;
  exerciseId: number;
  exercise: Exercise;
  sets?: number;
  reps?: number;
  weight?: number;
  duration?: number;
  orderIndex: number;
}

export interface SessionExerciseInput {
  exerciseId: number;
  sets?: number;
  reps?: number;
  weight?: number;
  duration?: number;
  orderIndex: number;
}
