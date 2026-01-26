'use client';

import { useState, useEffect, useCallback, use } from 'react';
import { useRouter } from 'next/navigation';
import {
  workoutPlansApi,
  workoutSessionsApi,
  WorkoutPlan,
  WorkoutSession,
  SessionExerciseInput,
} from '@/lib/api';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { ArrowLeft, Loader2, Save } from 'lucide-react';
import SessionExerciseEditor from '@/components/session-exercise-editor';
import SessionStatusBadge from '@/components/session-status-badge';
import { toast } from 'sonner';

interface PageProps {
  params: Promise<{ planId: string; sessionId: string }>;
}

export default function ExercisesPage({ params }: PageProps) {
  const { planId, sessionId } = use(params);
  const router = useRouter();
  const [plan, setPlan] = useState<WorkoutPlan | null>(null);
  const [session, setSession] = useState<WorkoutSession | null>(null);
  const [exercises, setExercises] = useState<SessionExerciseInput[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);

  const fetchData = useCallback(async () => {
    try {
      const [planRes, sessionsRes] = await Promise.all([
        workoutPlansApi.getById(Number(planId)),
        workoutSessionsApi.getByPlanId(Number(planId)),
      ]);
      setPlan(planRes.data);
      
      const foundSession = sessionsRes.data.find(
        (s: WorkoutSession) => s.id === Number(sessionId)
      );
      
      if (foundSession) {
        setSession(foundSession);
        // Map existing exercises to input format
        const existingExercises: SessionExerciseInput[] = (foundSession.sessionExercises || []).map(
          (se) => ({
            exerciseId: se.exerciseId,
            sets: se.sets,
            reps: se.reps,
            weight: se.weight,
            duration: se.duration,
            orderIndex: se.orderIndex,
          })
        );
        setExercises(existingExercises);
      } else {
        router.push(`/plans/${planId}/sessions`);
      }
    } catch {
      router.push('/dashboard');
    } finally {
      setIsLoading(false);
    }
  }, [planId, sessionId, router]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleSave = async () => {
    if (!session) return;

    // Validate that all exercises have an exerciseId selected
    const invalidExercises = exercises.filter((e) => !e.exerciseId || e.exerciseId === 0);
    if (invalidExercises.length > 0) {
      toast.error('Please select an exercise for all items');
      return;
    }

    setIsSaving(true);
    try {
      await workoutSessionsApi.update(session.id, {
        name: session.name,
        notes: session.notes,
        workoutDate: session.workoutDate,
        status: session.status,
        workoutPlanId: Number(planId),
        sessionExercises: exercises,
      });
      toast.success('Exercises saved successfully');
    } catch {
      // Error handled by interceptor
    } finally {
      setIsSaving(false);
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      weekday: 'long',
      month: 'long',
      day: 'numeric',
      year: 'numeric',
    });
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-20">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-4">
        <Button
          variant="ghost"
          size="icon"
          onClick={() => router.push(`/plans/${planId}/sessions`)}
        >
          <ArrowLeft className="h-5 w-5" />
          <span className="sr-only">Back to sessions</span>
        </Button>
        <div className="flex-1">
          <div className="flex items-center gap-3 flex-wrap">
            <h1 className="text-3xl font-bold text-foreground">{session?.name}</h1>
            {session && <SessionStatusBadge status={session.status} />}
          </div>
          <p className="text-muted-foreground mt-1">
            {plan?.name} &bull; {session && formatDate(session.workoutDate)}
          </p>
        </div>
        <Button onClick={handleSave} disabled={isSaving} className="gap-2">
          {isSaving ? (
            <>
              <Loader2 className="h-4 w-4 animate-spin" />
              Saving...
            </>
          ) : (
            <>
              <Save className="h-4 w-4" />
              Save Exercises
            </>
          )}
        </Button>
      </div>

      <Card className="bg-card border-border">
        <CardHeader>
          <CardTitle className="text-card-foreground">Manage Exercises</CardTitle>
          <CardDescription>
            Add, remove, and reorder exercises for this session. Set the number of sets, reps,
            weight, and duration for each exercise.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <SessionExerciseEditor exercises={exercises} onChange={setExercises} />
        </CardContent>
      </Card>

      {session?.notes && (
        <Card className="bg-card border-border">
          <CardHeader>
            <CardTitle className="text-card-foreground text-sm">Session Notes</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-muted-foreground text-sm">{session.notes}</p>
          </CardContent>
        </Card>
      )}
    </div>
  );
}
