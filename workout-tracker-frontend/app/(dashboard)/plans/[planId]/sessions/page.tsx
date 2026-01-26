'use client';

import { useState, useEffect, useCallback, use } from 'react';
import { useRouter } from 'next/navigation';
import {
  workoutPlansApi,
  workoutSessionsApi,
  WorkoutPlan,
  WorkoutSession,
  WorkoutSessionInput,
} from '@/lib/api';
import { Button } from '@/components/ui/button';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { ArrowLeft, Plus, Edit, Trash2, Loader2, Calendar, Dumbbell } from 'lucide-react';
import SessionStatusBadge from '@/components/session-status-badge';
import WorkoutSessionModal from '@/components/workout-session-modal';
import DeleteConfirmModal from '@/components/delete-confirm-modal';
import Link from 'next/link';
import { toast } from 'sonner';

interface PageProps {
  params: Promise<{ planId: string }>;
}

export default function SessionsPage({ params }: PageProps) {
  const { planId } = use(params);
  const router = useRouter();
  const [plan, setPlan] = useState<WorkoutPlan | null>(null);
  const [sessions, setSessions] = useState<WorkoutSession[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingSession, setEditingSession] = useState<WorkoutSession | null>(null);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [deletingSession, setDeletingSession] = useState<WorkoutSession | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);

  const fetchData = useCallback(async () => {
    try {
      const [planRes, sessionsRes] = await Promise.all([
        workoutPlansApi.getById(Number(planId)),
        workoutSessionsApi.getByPlanId(Number(planId)),
      ]);
      setPlan(planRes.data);
      setSessions(sessionsRes.data);
    } catch {
      router.push('/dashboard');
    } finally {
      setIsLoading(false);
    }
  }, [planId, router]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleCreateOrUpdate = async (data: Omit<WorkoutSessionInput, 'sessionExercises'>) => {
    const fullData: WorkoutSessionInput = {
      ...data,
      sessionExercises: editingSession?.sessionExercises?.map((se) => ({
        exerciseId: se.exerciseId,
        sets: se.sets,
        reps: se.reps,
        weight: se.weight,
        duration: se.duration,
        orderIndex: se.orderIndex,
      })) || [],
    };

    if (editingSession) {
      await workoutSessionsApi.update(editingSession.id, fullData);
      toast.success('Session updated successfully');
    } else {
      await workoutSessionsApi.create(fullData);
      toast.success('Session created successfully');
    }
    setEditingSession(null);
    fetchData();
  };

  const handleEdit = (session: WorkoutSession) => {
    setEditingSession(session);
    setModalOpen(true);
  };

  const handleDelete = (session: WorkoutSession) => {
    setDeletingSession(session);
    setDeleteModalOpen(true);
  };

  const confirmDelete = async () => {
    if (!deletingSession) return;
    setIsDeleting(true);
    try {
      await workoutSessionsApi.delete(deletingSession.id);
      toast.success('Session deleted successfully');
      setDeleteModalOpen(false);
      setDeletingSession(null);
      fetchData();
    } finally {
      setIsDeleting(false);
    }
  };

  const handleOpenCreate = () => {
    setEditingSession(null);
    setModalOpen(true);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      weekday: 'short',
      month: 'short',
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
        <Button variant="ghost" size="icon" onClick={() => router.push('/dashboard')}>
          <ArrowLeft className="h-5 w-5" />
          <span className="sr-only">Back to dashboard</span>
        </Button>
        <div className="flex-1">
          <h1 className="text-3xl font-bold text-foreground">{plan?.name}</h1>
          {plan?.description && (
            <p className="text-muted-foreground mt-1">{plan.description}</p>
          )}
        </div>
        <Button onClick={handleOpenCreate} className="gap-2">
          <Plus className="h-4 w-4" />
          New Session
        </Button>
      </div>

      {sessions.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-20 text-center">
          <div className="rounded-full bg-muted p-6 mb-4">
            <Calendar className="h-12 w-12 text-muted-foreground" />
          </div>
          <h3 className="text-xl font-semibold text-foreground mb-2">No sessions yet</h3>
          <p className="text-muted-foreground mb-6 max-w-md">
            Create your first workout session to start tracking your exercises.
          </p>
          <Button onClick={handleOpenCreate} className="gap-2">
            <Plus className="h-4 w-4" />
            Create First Session
          </Button>
        </div>
      ) : (
        <div className="rounded-lg border border-border bg-card overflow-hidden">
          <Table>
            <TableHeader>
              <TableRow className="hover:bg-transparent">
                <TableHead className="text-muted-foreground">Name</TableHead>
                <TableHead className="text-muted-foreground">Date</TableHead>
                <TableHead className="text-muted-foreground">Status</TableHead>
                <TableHead className="text-muted-foreground">Notes</TableHead>
                <TableHead className="text-muted-foreground text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {sessions.map((session) => (
                <TableRow key={session.id} className="hover:bg-muted/50">
                  <TableCell className="font-medium text-foreground">{session.name}</TableCell>
                  <TableCell className="text-muted-foreground">
                    {formatDate(session.workoutDate)}
                  </TableCell>
                  <TableCell>
                    <SessionStatusBadge status={session.status} />
                  </TableCell>
                  <TableCell className="text-muted-foreground max-w-[200px] truncate">
                    {session.notes || '-'}
                  </TableCell>
                  <TableCell className="text-right">
                    <div className="flex items-center justify-end gap-2">
                      <Link href={`/plans/${planId}/sessions/${session.id}/exercises`}>
                        <Button variant="outline" size="sm" className="gap-2 bg-transparent">
                          <Dumbbell className="h-4 w-4" />
                          <span className="hidden sm:inline">Exercises</span>
                        </Button>
                      </Link>
                      <Button
                        variant="ghost"
                        size="icon"
                        onClick={() => handleEdit(session)}
                      >
                        <Edit className="h-4 w-4" />
                        <span className="sr-only">Edit</span>
                      </Button>
                      <Button
                        variant="ghost"
                        size="icon"
                        onClick={() => handleDelete(session)}
                        className="text-destructive hover:text-destructive hover:bg-destructive/10"
                      >
                        <Trash2 className="h-4 w-4" />
                        <span className="sr-only">Delete</span>
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      )}

      <WorkoutSessionModal
        open={modalOpen}
        onOpenChange={setModalOpen}
        session={editingSession}
        planId={Number(planId)}
        onSubmit={handleCreateOrUpdate}
      />

      <DeleteConfirmModal
        open={deleteModalOpen}
        onOpenChange={setDeleteModalOpen}
        title="Delete Session"
        description={`Are you sure you want to delete "${deletingSession?.name}"? This action cannot be undone.`}
        onConfirm={confirmDelete}
        isLoading={isDeleting}
      />
    </div>
  );
}
